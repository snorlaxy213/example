package org.remo;

import io.grpc.stub.StreamObserver;
import org.remo.user.*;

import java.util.HashMap;
import java.util.Map;

@GrpcService
public class RPCUserServiceImpl extends RPCUserServiceGrpc.RPCUserServiceImplBase {

    private static final Map<Integer, GetUserInfoResponse> userResponseMaps = new HashMap<>();

    static {
        userResponseMaps.put(1, GetUserInfoResponse.newBuilder().setUserId(1).setUserName("john").setPhone("13858").build());
        userResponseMaps.put(2, GetUserInfoResponse.newBuilder().setUserId(2).setUserName("john").setPhone("24685").build());
        userResponseMaps.put(3, GetUserInfoResponse.newBuilder().setUserId(3).setUserName("john").setPhone("13579").build());
    }

    @Override
    public void getUser(GetUserInfoRequest request, StreamObserver<GetUserInfoResponse> responseObserver) {
        GetUserInfoResponse userInfoResponse = null;
        try {
            userInfoResponse = userResponseMaps.get(request.getUserId());
        } catch (Exception e) {
            responseObserver.onError(e);
        } finally {
            responseObserver.onNext(userInfoResponse);
        }
        responseObserver.onCompleted();
    }

    @Override
    public void listUser(ListUserInfoRequest request, StreamObserver<ListUserInfoResponse> responseObserver) {
        ListUserInfoResponse userInfoResponse = null;
        try {
            userInfoResponse = ListUserInfoResponse.newBuilder().addAllUsers(userResponseMaps.values()).build();
        } catch (Exception e) {
            responseObserver.onError(e);
        } finally {
            responseObserver.onNext(userInfoResponse);
        }
        responseObserver.onCompleted();
    }
}
