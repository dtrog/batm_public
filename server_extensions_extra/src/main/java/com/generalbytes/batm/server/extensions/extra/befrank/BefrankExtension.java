/*************************************************************************************
 * Copyright (C) 2014-2018 GENERAL BYTES s.r.o. All rights reserved.
 *
 * This software may be distributed and modified under the terms of the GNU
 * General Public License version 2 (GPL2) as published by the Free Software
 * Foundation and appearing in the file GPL2.TXT included in the packaging of
 * this file. Please note that GPL2 Section 2[b] requires that all works based
 * on this software must also be made publicly available under the terms of
 * the GPL2 ("Copyleft").
 *
 * Contact information
 * -------------------
 *
 * GENERAL BYTES s.r.o.
 * Web      :  http://www.generalbytes.com
 *
 ************************************************************************************/
package com.generalbytes.batm.server.extensions.extra.befrank;

import com.generalbytes.batm.server.extensions.*;
import com.generalbytes.batm.server.extensions.extra.befrank.sources.BefrankRateFix;
import com.generalbytes.batm.server.extensions.extra.befrank.wallets.befrankd.BefrankWallet;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

public class BefrankExtension extends AbstractExtension implements ICryptoAddressValidator {
    public static final String CURRENCY = Currencies.BFR;

    // 2 decimals
    public static final BigDecimal BFR_PRECISION = BigDecimal.valueOf(Math.pow(10, 2));

    @Override
    public String getName() {
        return "BATM Belgian e-Frank extra extension";
    }

    @Override
    public IWallet createWallet(String walletLogin) {
        if (walletLogin != null && !walletLogin.trim().isEmpty()) {
            StringTokenizer st = new StringTokenizer(walletLogin, ":");
            String walletType = st.nextToken();
            if ("befrankd".equalsIgnoreCase(walletType)) {

                String protocol = st.nextToken();
                String username = st.nextToken();
                String password = st.nextToken();
                String hostname = st.nextToken();
                String port = "";
                if (st.hasMoreTokens()) {
                    port = st.nextToken();
                }


                if (protocol != null && username != null && password != null && hostname != null && port != null) {
                    return new BefrankWallet(hostname, Integer.valueOf(port).intValue(), username, password);
                }
            }
        }
        return null;
    }

    @Override
    public Set<String> getSupportedCryptoCurrencies() {
        Set<String> result = new HashSet<String>();
        result.add(CURRENCY);
        return result;
    }

    @Override
    public ICryptoAddressValidator createAddressValidator(String cryptoCurrency) {
        if (CURRENCY.equalsIgnoreCase(cryptoCurrency)) {
            return new BefrankAddressValidator();
        }
        return null;
    }

    @Override
    public IRateSource createRateSource(String sourceLogin) {
        if (sourceLogin != null && !sourceLogin.trim().isEmpty()) {
            StringTokenizer st = new StringTokenizer(sourceLogin,":");
            String exchangeType = st.nextToken();

            if ("befrankfix".equalsIgnoreCase(exchangeType)) {
                BigDecimal rate = BigDecimal.ZERO;
                if (st.hasMoreTokens()) {
                    try {
                        rate = new BigDecimal(st.nextToken());
                    } catch (Throwable e) {
                    }
                }
                String preferedFiatCurrency = Currencies.EUR;
                if (st.hasMoreTokens()) {
                    preferedFiatCurrency = st.nextToken().toUpperCase();
                }
                return new BefrankRateFix(rate,preferedFiatCurrency);
            }

        }
        return null;
    }

    @Override
    public boolean isAddressValid(String address) {
        return address.startsWith("F",0);
    }

    @Override
    public boolean mustBeBase58Address() {
        return true;
    }

    @Override
    public boolean isPaperWalletSupported() {
        return false;
    }
}
