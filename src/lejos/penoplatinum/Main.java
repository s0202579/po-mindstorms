package penoplatinum;

import penoplatinum.navigators.BehaviourNavigator;

public class Main {

    public static void main(String[] args) throws Exception {
        Runnable runnable = new Runnable() {

            public void run() {
                Utils.Log("Started!");

                AngieEventLoop loop = new AngieEventLoop();
                loop.useNavigator(new BehaviourNavigator());
                //loop.useNavigator(new TurnNavigator());
                loop.runEventLoop();
            }
        };


        //RobotRunner.Run(runnable);
        runnable.run();

    }
}
