package info.nightscout.androidaps.plugins.DanaR.comm;

import com.squareup.otto.Bus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import info.nightscout.androidaps.MainApp;
import info.nightscout.androidaps.R;
import info.nightscout.androidaps.db.Treatment;
import info.nightscout.androidaps.plugins.Overview.events.EventOverviewBolusProgress;

public class MsgBolusStop extends MessageBase {
    private static Logger log = LoggerFactory.getLogger(MsgBolusStop.class);
    private static Treatment t;
    private static Double amount;
    private static Bus bus = null;

    public static boolean stopped = false;
    public static boolean forced = false;

    public MsgBolusStop() {
        SetCommand(0x0101);
        stopped = false;
    }

    public MsgBolusStop(Bus bus, Double amount, Treatment t) {
        this();
        this.bus = bus;
        this.t = t;
        this.amount = amount;
        forced = false;
    }

    @Override
    public void handleMessage(byte[] bytes) {
        EventOverviewBolusProgress bolusingEvent = EventOverviewBolusProgress.getInstance();
        stopped = true;
        if (!forced) {
            t.insulin = amount;
            bolusingEvent.status = MainApp.sResources.getString(R.string.overview_bolusprogress_delivered);
            bolusingEvent.percent = 100;
        } else {
            bolusingEvent.status = MainApp.sResources.getString(R.string.overview_bolusprogress_stoped);
        }
        bus.post(bolusingEvent);
    }
}
