package com.example.final_project.presentation.event

import com.example.final_project.presentation.model.Passcode

sealed interface PasscodeEvent {
    data class SignInWithVerificationCode(val verificationId: String, val smsCode: String) : PasscodeEvent
    data class SignUp(val verificationId: String, val smsCode: String) : PasscodeEvent
    class ChangeTextInputEvent(val passcode: Passcode) : PasscodeEvent
    object ResetPasscode : PasscodeEvent
}