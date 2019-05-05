package controler;

import appStates.InGameState;
import appStates.MultiPlayerLobbyState;
import com.jme3.input.controls.ActionListener;
import static appStates.MultiPlayerLobbyState.insertedIP;




public class MultiPlLobbyStateListener implements ActionListener
{
    @Override
    public void onAction(String name, boolean keyPressed, float tpf)
    {

        if(checkIfNumberOrPeriod(name) && !keyPressed)
            insertedIP +=name;

    }

    private boolean checkIfNumberOrPeriod(String name)
    {
        return ((name.charAt(0)>=48 && name.charAt(0)<=57 )|| name.equals("."));
    }

}
