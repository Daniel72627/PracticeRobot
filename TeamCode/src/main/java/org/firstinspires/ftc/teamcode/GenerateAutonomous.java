package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Autonomous(name = "GenerateAutonomous", group = "Robot")
public class GenerateAutonomous extends OpMode {

    private DcMotor BL, BR, FR, FL;
    private List<InputLog> logData;
    private int currentLogIndex = 0;
    private long startTime;

    @Override
    public void init() {
        // Initialize motors
        BL = hardwareMap.get(DcMotor.class, "BL");
        BR = hardwareMap.get(DcMotor.class, "BR");
        FR = hardwareMap.get(DcMotor.class, "FR");
        FL = hardwareMap.get(DcMotor.class, "FL");

        FL.setDirection(DcMotor.Direction.FORWARD);
        BL.setDirection(DcMotor.Direction.FORWARD);
        FR.setDirection(DcMotor.Direction.FORWARD);
        BR.setDirection(DcMotor.Direction.FORWARD);


        logData = new ArrayList<>();
        telemetry.addData("Status", "Initialized. Replay.");

        // Read log file
        try (BufferedReader reader = new BufferedReader(new FileReader("/sdcard/FIRST/log_file.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                InputLog log = new InputLog();
                log.timestamp = Long.parseLong(parts[0].split(":")[1].trim());
                log.BLPower = Double.parseDouble(parts[1].split(":")[1].trim());
                log.BRPower = Double.parseDouble(parts[2].split(":")[1].trim());
                log.FRPower = Double.parseDouble(parts[3].split(":")[1].trim());
                log.FLPower = Double.parseDouble(parts[4].split(":")[1].trim());
                logData.add(log);
            }
        } catch (IOException e) {
            telemetry.addData("Error", "Failed to load log file: " + e.getMessage());
        }
    }

    @Override
    public void start() {
        startTime = System.currentTimeMillis();
    }

    @Override
    public void loop() {
        //replay
        if (currentLogIndex < logData.size()) {
            InputLog log = logData.get(currentLogIndex);


            long elapsedTime = System.currentTimeMillis() - startTime;
            if (elapsedTime >= log.timestamp) {

                BL.setPower(log.BLPower);
                BR.setPower(log.BRPower);
                FR.setPower(log.FRPower);
                FL.setPower(log.FLPower);

                currentLogIndex++;
            }
        } else {

            stop();
        }


        telemetry.addData("Replaying", "Timestamp: %d ms", System.currentTimeMillis() - startTime);
        telemetry.addData("BL Power", BL.getPower());
        telemetry.addData("BR Power", BR.getPower());
        telemetry.addData("FR Power", FR.getPower());
        telemetry.addData("FL Power", FL.getPower());
        telemetry.update();
    }

    @Override
    public void stop() {

        BL.setPower(0);
        BR.setPower(0);
        FR.setPower(0);
        FL.setPower(0);
        telemetry.addData("Status", "Autonomous Replay Finished");
        telemetry.update();
    }


    private static class InputLog {
        long timestamp;
        double BLPower;
        double BRPower;
        double FRPower;
        double FLPower;
    }
}
