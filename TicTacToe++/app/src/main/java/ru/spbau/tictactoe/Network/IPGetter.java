package ru.spbau.tictactoe.Network;

import android.os.AsyncTask;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.List;

public class IPGetter extends AsyncTask<Void, Void, String> {

    Exception exception;

    @Override
    protected String doInBackground(Void... voids) {
        List<NetworkInterface> interfaces;

        try {
            interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
        } catch (SocketException e) {
            exception = e;
            return null;
        }

        for (NetworkInterface networkInterface : interfaces) {
            List<InetAddress> addresses = Collections.list(networkInterface.getInetAddresses());
            for (InetAddress address : addresses) {
                if (!address.isLoopbackAddress() && address instanceof Inet4Address) {
                    System.err.println("hostName: " + address.getHostName() +
                            " hostAddress: " + address.getHostAddress());
//                            return address.getHostAddress();
                }
            }
        }

        return "empty";
    }
}
