import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.remo.api.RPCDateRequest;
import org.remo.api.RPCDateResponse;
import org.remo.api.RPCDateServiceGrpc;
import org.remo.user.*;


public class GRPCClient {
    private static final String host = "localhost";
    private static final int serverPort = 9999;

    public static void main(String[] args) {
        ManagedChannel managedChannel = ManagedChannelBuilder.forAddress(host, serverPort).usePlaintext().build();
        try {
            RPCDateServiceGrpc.RPCDateServiceBlockingStub rpcDateService = RPCDateServiceGrpc.newBlockingStub(managedChannel);
            RPCDateRequest rpcDateRequest = RPCDateRequest
                    .newBuilder()
                    .setUserName("shgx")
                    .build();
            RPCDateResponse rpcDateResponse = rpcDateService.getDate(rpcDateRequest);
            System.out.println(rpcDateResponse.getServerDate());

            //------------------ GetUser ------------------
            RPCUserServiceGrpc.RPCUserServiceBlockingStub userService = RPCUserServiceGrpc.newBlockingStub(managedChannel);
            GetUserInfoRequest getUserInfoRequest = GetUserInfoRequest.newBuilder().setUserId(1).build();
            GetUserInfoResponse getUserInfoResponse = userService.getUser(getUserInfoRequest);
            System.out.println(getUserInfoResponse.getUserName());

            //------------------ GetUser ------------------
            ListUserInfoRequest listUserInfoRequest = ListUserInfoRequest.newBuilder().build();
            ListUserInfoResponse listUserInfoResponse = userService.listUser(listUserInfoRequest);
            System.out.println(listUserInfoResponse.getUsersList());
        } finally {
            managedChannel.shutdown();
        }
    }
}
