package com.generalbytes.batm.server.extensions.extra.befrank.wallets.befrankd;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.IOException;

@Path("/json_rpc")
@Produces(MediaType.APPLICATION_JSON)
public interface BefrankWalletAPI {

    @POST
    @Path("/{id}/{method}/{params}/{jsonrpc}")
    default String getJsonRpc(@PathParam("id") String id, @PathParam("method") String method, @PathParam("params") String params, @PathParam("jsonrpc") String jsonRpc) throws IOException {
        return null;
    }
}