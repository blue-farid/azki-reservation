package com.azki.reservation.service.impl;

import com.azki.reservation.api.v1.request.LoginOrSignupRequest;
import com.azki.reservation.api.v1.request.LoginWithPasswordRequest;
import com.azki.reservation.api.v1.request.OtpRequest;
import com.azki.reservation.api.v1.request.SetPasswordRequest;
import com.azki.reservation.api.v1.response.LoginOrSignupResponse;
import com.azki.reservation.config.properties.SecurityProperties;
import com.azki.reservation.constant.AuthoritiesConstant;
import com.azki.reservation.domain.Customer;
import com.azki.reservation.exception.auth.ConfirmPasswordException;
import com.azki.reservation.exception.auth.InvalidEmailOrPasswordException;
import com.azki.reservation.exception.auth.InvalidOtpException;
import com.azki.reservation.exception.customer.CustomerNotFoundException;
import com.azki.reservation.mapper.CustomerMapper;
import com.azki.reservation.repository.CustomerRepository;
import com.azki.reservation.service.AuthenticationService;
import com.azki.reservation.util.JwtUtil;
import com.azki.reservation.util.MailUtil;
import com.azki.reservation.util.RedisUtil;
import com.azki.reservation.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final CustomerRepository customerRepository;
    private final CustomerMapper mapper;
    private final RedisUtil redisUtil;
    private final SecurityUtil securityUtil;
    private final MailUtil mailUtil;
    private final JwtUtil jwtUtil;
    private final SecurityProperties securityProperties;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public LoginOrSignupResponse login(LoginOrSignupRequest request) {
        String otp = redisUtil.getValue(request.getMail());

        if (!StringUtils.hasText(otp) || !otp.equals(request.getOtp())) {
            throw new InvalidOtpException();
        }

        Optional<Customer> optCustomer = customerRepository.findByMail(request.getMail());
        Customer customer;
        customer = optCustomer.orElseGet(() -> newCustomer(request));

        return new LoginOrSignupResponse()
                .setCustomer(mapper.entityToDto(customer))
                .setToken(jwtUtil.generateCustomerToken(customer.getId(), customer.getMail(),
                        AuthoritiesConstant.CUSTOMER));
    }

    @Override
    public LoginOrSignupResponse loginWithPassword(LoginWithPasswordRequest request) {
        Optional<Customer> customer = customerRepository.findByMail(request.getMail());

        if (customer.isEmpty() || (StringUtils.hasText(customer.get().getPassword())
                && customer.get().getPassword().equals(passwordEncoder.encode(request.getPassword())))) {
            throw new InvalidEmailOrPasswordException();
        }

        return new LoginOrSignupResponse()
                .setCustomer(mapper.entityToDto(customer.get()))
                .setToken(jwtUtil.generateCustomerToken(customer.get().getId(),
                        customer.get().getMail(),
                        AuthoritiesConstant.CUSTOMER));

    }

    @Override
    @Transactional
    public void setPassword(SetPasswordRequest request) {
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new ConfirmPasswordException();
        }

        customerRepository.save(customerRepository.findById(request.getId())
                .orElseThrow(CustomerNotFoundException::new)
                .setPassword(passwordEncoder.encode(request.getPassword())));
    }

    @Override
    public void sendOtp(OtpRequest request) {
        String otp = securityUtil.generateOtp();
        redisUtil.insert(request.getMail(), otp, securityProperties.getOtpExpiration());
        mailUtil.sendOtp(request.getMail(), otp);
    }

    private Customer newCustomer(LoginOrSignupRequest request) {
        return customerRepository.save(new Customer().setMail(request.getMail()));
    }
}
