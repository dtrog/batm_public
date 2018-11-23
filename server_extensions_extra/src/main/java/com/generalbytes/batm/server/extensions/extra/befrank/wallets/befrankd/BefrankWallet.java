package com.generalbytes.batm.server.extensions.extra.befrank.wallets.befrankd;

import com.generalbytes.batm.server.extensions.IWallet;
import com.generalbytes.batm.server.extensions.extra.befrank.BefrankExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import static si.mazi.rescu.RestProxyFactory.createProxy;

public class BefrankWallet implements IWallet {
    private static final Logger log = LoggerFactory.getLogger(BefrankWallet.class);
    private static final String walletFile = "batmwallet.bin";
    private static  BefrankWalletAPI api;
    private String host = "localhost";
    private int port = 18324;
    private String rpcUser;
    private String rpcPassword;
    private String filePassword = "af1ff536add73ef7ead85a0dbb2f811eb4f4fe8c10102580e15eac515639c5c1";

    public BefrankWallet(String host, int port, String rpcUser, String rpcPassword) {
        this.host = host;
        this.port = port;
        this.rpcUser = rpcUser;
        this.rpcPassword = rpcPassword;
        api = createProxy(BefrankWalletAPI.class, "http://" + rpcUser +":" + rpcPassword + "@" + host+":"+port);
    }

    @Override
    public Set<String> getCryptoCurrencies() {
        Set<String> result = new HashSet<String>();
        result.add(BefrankExtension.CURRENCY);
        return result;

    }

    @Override
    public String getPreferredCryptoCurrency() {
        return BefrankExtension.CURRENCY;
    }

    @Override
    public String sendCoins(String destinationAddress, BigDecimal amount, String cryptoCurrency, String description) {
        StringBuilder sb = new StringBuilder();
        sb.append("'params' : {");
        sb.append("     'destinations': [ ");
        sb.append("          {");
        sb.append("             'amount'  : " + amount.toString() + ", ");
        sb.append("             'address' : '" + destinationAddress + "'");
        sb.append("          }");
        sb.append("     ],");
        sb.append("     'payment_id': '',");
        sb.append("     'fee': 100,");
        sb.append("     'mixin': 0,");
        sb.append("     'unlock_time': 0");
        sb.append("}");
        String params = sb.toString();

        String result = null;
        try {
            result = api.getJsonRpc("batm",
                "transfer",
                 params,
                "2.0");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public String getCryptoAddress(String cryptoCurrency) {
        String address = null;
        try {
            address = api.getJsonRpc("batm", "get_address", "", "2.0");
            return address;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return address;
    }

    @Override
    public BigDecimal getCryptoBalance(String cryptoCurrency) {
        try {
            String balance = api.getJsonRpc("batm", "getbalance", "{}", "2.0");
            return BigDecimal.valueOf(Long.valueOf(balance));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return null;
    }

}
