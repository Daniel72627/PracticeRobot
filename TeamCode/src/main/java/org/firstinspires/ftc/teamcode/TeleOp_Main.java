package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@TeleOp(name = "TeleOp_Main", group = "Robot")
public class TeleOp_Main extends OpMode {
    public DcMotor BL, BR, FR, FL;
    @Override
    public void init() {
        // Initialize motors
        BL = hardwareMap.get(DcMotor.class, "BL");
        BR = hardwareMap.get(DcMotor.class, "BR");
        FR = hardwareMap.get(DcMotor.class, "FR");
        FL = hardwareMap.get(DcMotor.class, "FL");

        BL.setDirection(DcMotor.Direction.FORWARD);
        BR.setDirection(DcMotor.Direction.FORWARD);
        FR.setDirection(DcMotor.Direction.FORWARD);
        FL.setDirection(DcMotor.Direction.FORWARD);
        telemetry.addData("Status", "Initialized Yupi");
    }

    @Override
    public void start() {

    }

    @Override
    public void loop() {

        //Movement and Wheels
        if (gamepad1.dpad_up) {
            setMotors(1, 1, 1, 1);  // forward
        } else if (gamepad1.dpad_down) {
            setMotors(-1, -1, -1, -1);  // down
        } else if (gamepad1.dpad_left) {
            setMotors(-1, 1, -1, 1);  // left
        } else if (gamepad1.dpad_right) {
            setMotors(1, -1, 1, -1);  // right
        } else {
            setMotors(0, 0, 0, 0);
        }

        if(gamepad1.left_bumper) {
            setMotors(-1,1,1,-1);
        }

        if(gamepad1.right_bumper) {
            setMotors(1,-1,-1,1);
        }
    }

    @Override
    public void stop() {
        setMotors(0,0,0,0);
    }

    public void setMotors(int fl, int fr, int br, int bl) {
        FL.setPower(fl);
        FR.setPower(fr);
        BR.setPower(br);
        BL.setPower(bl);
    }

}
