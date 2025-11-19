package com.azki.reservation.service;

import com.azki.reservation.api.v1.request.LoginOrSignupRequest;
import com.azki.reservation.api.v1.request.LoginWithPasswordRequest;
import com.azki.reservation.api.v1.request.OtpRequest;
import com.azki.reservation.api.v1.request.SetPasswordRequest;
import com.azki.reservation.api.v1.response.LoginOrSignupResponse;

public interface AuthenticationService {
    LoginOrSignupResponse login(LoginOrSignupRequest request);

    LoginOrSignupResponse loginWithPassword(LoginWithPasswordRequest request);

    void setPassword(SetPasswordRequest request);

    void sendOtp(OtpRequest request);
}
