/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulator.interfaces;

import simulator.SimulatorMap;

/**
 *
 * @author 82wach1bif
 */
public interface IClientCallback {

    void onMessage(String msg);

    void onMapLoaded(SimulatorMap map);

    void onGameFinished();
}
