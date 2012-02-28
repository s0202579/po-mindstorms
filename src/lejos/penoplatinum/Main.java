package penoplatinum;

import java.io.PrintStream;
import lejos.nxt.Button;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import penoplatinum.sensor.IRSeekerV2.Mode;
import penoplatinum.bluetooth.PacketTransporter;
import penoplatinum.bluetooth.RobotBluetoothConnection;
import penoplatinum.navigators.SectorNavigator;
import penoplatinum.sensor.IRSeekerV2;

public class Main {

    public static void main(String[] args) throws Exception {


        final AngieEventLoop angie = new AngieEventLoop();
        
        initializeAgent(angie);
        
        Runnable robot = new Runnable() {
            
            public void run() {
                Utils.Log("Started!");
                angie.useNavigator(new SectorNavigator());
                angie.runEventLoop();
            }
        };
        
        robot.run();
    }

    private static boolean startMeasurement(IRSeekerV2 seeker, int[] angles, Motor m, int startAngle) {
        while (!Button.ESCAPE.isPressed()) {
            for (int i = 0; i < angles.length; i++) {
                int angle = angles[i];
                m.rotateTo(startAngle + angle, false);
                int dir = seeker.getDirection();
                Utils.Log(angle + ", " + dir);
                for(int j=1; j<6; j++){
                    Utils.Log(""+j+":"+seeker.getSensorValue(j));
                }
                if (Button.ESCAPE.isPressed()) {
                    return true;
                }
                Utils.Sleep(1000);
            }
        }
        return false;
    }
    static byte[] buf = new byte[1];

    private static void runRobotSemester1() {
        final AngieEventLoop angie = new AngieEventLoop();

        initializeAgent(angie);

        Runnable robot = new Runnable() {

            public void run() {
                Utils.Log("Started!");
                angie.useNavigator(new SectorNavigator());
                angie.runEventLoop();
            }
        };

        robot.run();
    }

    private static void initializeAgent(final AngieEventLoop angie) {
        RobotBluetoothConnection connection = new RobotBluetoothConnection();
        connection.initializeConnection();
//        Utils.EnableRemoteLogging(connection);
        final PacketTransporter transporter = new PacketTransporter(connection);
        
        connection.RegisterTransporter(transporter, 123);
        final PrintStream stream = new PrintStream(transporter.getSendStream());


        Runnable communication = new Runnable() {

            public void run() {
                try {
                    while (true) {
                        String state = angie.fetchState();
                        stream.println(state);

                        transporter.SendPacket(123);
                        Utils.Sleep(30);
                    }
                } catch (Exception e) {
                    Utils.Log("Comm crashed!");
                }
            }
        };
        Thread t = new Thread(communication);
        t.start();

    }
}
