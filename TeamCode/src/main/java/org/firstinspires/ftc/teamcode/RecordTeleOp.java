package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@TeleOp(name = "RecordTeleOp", group = "Robot")
public class RecordTeleOp extends OpMode {

    public DcMotor BL, BR, FR, FL;
    public boolean d_up, d_down, d_left, d_right;
    private List<InputLog> logData;
    private long startTime;

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


        logData = new ArrayList<>();
        telemetry.addData("Status", "Initialized Yupi");
    }

    @Override
    public void start() {
        // Record start time for timestamp calculation
        startTime = System.currentTimeMillis();
    }

    @Override
    public void loop() {

        d_up = gamepad1.dpad_up;
        d_down = gamepad1.dpad_down;
        d_right = gamepad1.dpad_right;
        d_left = gamepad1.dpad_left;


        if (d_up) {
            setMotors(1, 1, 1, 1);  // forward
        } else if (d_down) {
            setMotors(-1, -1, -1, -1);  // down
        } else if (d_left) {
            setMotors(-1, 1, -1, 1);  // left
        } else if (d_right) {
            setMotors(1, -1, 1, -1);  // right
        } else {
            setMotors(0, 0, 0, 0);
        }

        //---------------------------------------------------------LEAVE THIS ALONE
        double BLPower = BL.getPower();
        double BRPower = BR.getPower();
        double FRPower = FR.getPower();
        double FLPower = FL.getPower();

        InputLog log = new InputLog();
        log.timestamp = System.currentTimeMillis() - startTime;

        log.BLPower = BLPower;
        log.BRPower = BRPower;
        log.FRPower = FRPower;
        log.FLPower = FLPower;

        logData.add(log);

        telemetry.addData("Recording", "Timestamp: %d ms", log.timestamp);
        telemetry.addData("BL Power", BLPower);
        telemetry.addData("BR Power", BRPower);
        telemetry.addData("FR Power", FRPower);
        telemetry.addData("FL Power", FLPower);
        telemetry.update();
    }

    @Override
    public void stop() {
        // Write the logs to a plain text file when recording stops
        File directory = new File("/sdcard/FIRST"); // "/sdcard/FIRST" maybe idk

        if(!directory.exists()) {
            directory.mkdirs();
        }

        File filePath = new File(directory, "log_file.txt");


        try (FileWriter writer = new FileWriter(filePath)) {
            for (InputLog log : logData) {
                writer.write(String.format("Timestamp: %d, BLPower: %.2f, BRPower: %.2f, FRPower: %.2f, FLPower: %.2f\n",
                        log.timestamp, log.BLPower, log.BRPower, log.FRPower, log.FLPower));
            }
            telemetry.addData("Status: ", "Recording saved to %s", filePath);
        } catch (IOException e) {
            telemetry.addData("Error: ", "Failed Recording" + e.getMessage());
        }
        telemetry.update();
    }

    // Helper class to store input log entries
    private static class InputLog {
        long timestamp;
        double BLPower;
        double BRPower;
        double FRPower;
        double FLPower;
    }

    public void setMotors(int fl, int fr, int br, int bl) {
        FL.setPower(fl);
        FR.setPower(fr);
        BR.setPower(br);
        BL.setPower(bl);
    }
}
