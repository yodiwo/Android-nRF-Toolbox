package com.yodiwo.androidnode;

import android.content.Context;
import android.nfc.NfcAdapter;

import com.yodiwo.plegma.ConfigParameter;
import com.yodiwo.plegma.Port;
import com.yodiwo.plegma.PortKey;
import com.yodiwo.plegma.Thing;
import com.yodiwo.plegma.ThingKey;
import com.yodiwo.plegma.ThingUIHints;
import com.yodiwo.plegma.ePortConf;
import com.yodiwo.plegma.ePortType;
import com.yodiwo.plegma.ioPortDirection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by FxBit on 7/8/2015.
 */
public class ThingManager {

    // =============================================================================================
    // Static information

    public static final String TAG = ThingManager.class.getSimpleName();
    private static ThingManager thingManager = null;


    public static ThingManager getInstance(Context context) {
        if (thingManager == null) {
            thingManager = new ThingManager(context.getApplicationContext());
        }

        return thingManager;
    }

    // =============================================================================================
    // Things names

    public static final String NordicUart = "NordicUart";
    public static final int NordicUartPortRx = 0;
    public static final int NordicUartPortTx = 1;

    // =============================================================================================
    // Things Initialization

    private SettingsProvider settingsProvider;
    private Context context;

    public ThingManager(Context context) {
        this.context = context;
        this.settingsProvider = SettingsProvider.getInstance(context);
    }

    // ---------------------------------------------------------------------------------------------

    // Here we initialize all things and add them to node service
    // Any new things must be added here.
    public void RegisterThings() {
        String nodeKey = settingsProvider.getNodeKey();
        String thingKey = "";
        Thing thing = null;

        // Clean old local things
        NodeService.CleanThings(context);

        // ----------------------------------------------
        // UART

        thingKey = ThingKey.CreateKey(nodeKey, NordicUart);
        thing = new Thing(thingKey, NordicUart, new ArrayList<ConfigParameter>(), new ArrayList<Port>(), "", "",
                new ThingUIHints("/Content/VirtualGateway/img/icon-uart.png", ""));

        thing.Ports.add(new Port(PortKey.CreateKey(thingKey, Integer.toString(NordicUartPortRx)),
                "Rx", "",
                ioPortDirection.Output, ePortType.String, "0", 0, ePortConf.None));

        thing.Ports.add(new Port(PortKey.CreateKey(thingKey, Integer.toString(NordicUartPortTx)),
                "Tx", "",
                ioPortDirection.Input, ePortType.String, "0", 0, ePortConf.ReceiveAllEvents));

        NodeService.AddThing(context, thing);
    }

    // ---------------------------------------------------------------------------------------------

    public String GetThingKey(String thingId) {
        String nodeKey = settingsProvider.getNodeKey();
        return ThingKey.CreateKey(nodeKey, thingId);
    }

    // ---------------------------------------------------------------------------------------------

    public String GetPortKey(String thingId, int portId) {
        String nodeKey = settingsProvider.getNodeKey();
        String thingKey = ThingKey.CreateKey(nodeKey, thingId);
        return PortKey.CreateKey(thingKey, Integer.toString(portId));
    }

    // ---------------------------------------------------------------------------------------------

}
