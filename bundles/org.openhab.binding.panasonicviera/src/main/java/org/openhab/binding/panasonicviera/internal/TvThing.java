package org.openhab.binding.panasonicviera.internal;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

public class TvThing {

    private final RemoteControl remoteControl;
    private AtomicBoolean poweredOn = new AtomicBoolean(false);

    public TvThing(RemoteControl remoteControl) {
        this.remoteControl = remoteControl;
    }

    public boolean refreshState() {
        try {
            poweredOn.set(remoteControl.getPowerStatus());
            return true;
        } catch (IOException e) {
            return false;
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isPoweredOn() {
        return poweredOn.get();
    }

    public void powerOn() {
        if (!isPoweredOn()) {
            try {
                remoteControl.sendKey(Key.POWER);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ParserConfigurationException e) {
                throw new RuntimeException(e);
            } catch (SAXException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void powerOff() {
        if (isPoweredOn()) {
            try {
                remoteControl.sendKey(Key.POWER);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ParserConfigurationException e) {
                throw new RuntimeException(e);
            } catch (SAXException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void setPower(boolean value) {
        if (value) {
            powerOn();
        } else {
            powerOff();
        }
    }
}
