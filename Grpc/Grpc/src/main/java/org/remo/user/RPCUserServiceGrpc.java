package org.remo.user;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

/**
 * <pre>
 * 服务接口定义，服务端和客户端都要遵守该接口进行通信
 * </pre>
 */
@javax.annotation.Generated(
        value = "by gRPC proto compiler (version 1.2.0)",
        comments = "Source: user.proto")
public final class RPCUserServiceGrpc {

    public static final String SERVICE_NAME = "org.remo.user.RPCUserService";
    // Static method descriptors that strictly reflect the proto.
    @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
    public static final io.grpc.MethodDescriptor<GetUserInfoRequest,
            GetUserInfoResponse> METHOD_GET_USER =
            io.grpc.MethodDescriptor.create(
                    io.grpc.MethodDescriptor.MethodType.UNARY,
                    generateFullMethodName(
                            "org.remo.user.RPCUserService", "getUser"),
                    io.grpc.protobuf.ProtoUtils.marshaller(GetUserInfoRequest.getDefaultInstance()),
                    io.grpc.protobuf.ProtoUtils.marshaller(GetUserInfoResponse.getDefaultInstance()));
    @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
    public static final io.grpc.MethodDescriptor<ListUserInfoRequest,
            ListUserInfoResponse> METHOD_LIST_USER =
            io.grpc.MethodDescriptor.create(
                    io.grpc.MethodDescriptor.MethodType.UNARY,
                    generateFullMethodName(
                            "org.remo.user.RPCUserService", "listUser"),
                    io.grpc.protobuf.ProtoUtils.marshaller(ListUserInfoRequest.getDefaultInstance()),
                    io.grpc.protobuf.ProtoUtils.marshaller(ListUserInfoResponse.getDefaultInstance()));
    private static final int METHODID_GET_USER = 0;
    private static final int METHODID_LIST_USER = 1;
    private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

    private RPCUserServiceGrpc() {
    }

    /**
     * Creates a new async stub that supports all call types for the service
     */
    public static RPCUserServiceStub newStub(io.grpc.Channel channel) {
        return new RPCUserServiceStub(channel);
    }

    /**
     * Creates a new blocking-style stub that supports unary and streaming output calls on the service
     */
    public static RPCUserServiceBlockingStub newBlockingStub(
            io.grpc.Channel channel) {
        return new RPCUserServiceBlockingStub(channel);
    }

    /**
     * Creates a new ListenableFuture-style stub that supports unary and streaming output calls on the service
     */
    public static RPCUserServiceFutureStub newFutureStub(
            io.grpc.Channel channel) {
        return new RPCUserServiceFutureStub(channel);
    }

    public static io.grpc.ServiceDescriptor getServiceDescriptor() {
        io.grpc.ServiceDescriptor result = serviceDescriptor;
        if (result == null) {
            synchronized (RPCUserServiceGrpc.class) {
                result = serviceDescriptor;
                if (result == null) {
                    serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
                            .setSchemaDescriptor(new RPCUserServiceDescriptorSupplier())
                            .addMethod(METHOD_GET_USER)
                            .addMethod(METHOD_LIST_USER)
                            .build();
                }
            }
        }
        return result;
    }

    /**
     * <pre>
     * 服务接口定义，服务端和客户端都要遵守该接口进行通信
     * </pre>
     */
    public static abstract class RPCUserServiceImplBase implements io.grpc.BindableService {

        /**
         *
         */
        public void getUser(GetUserInfoRequest request,
                            io.grpc.stub.StreamObserver<GetUserInfoResponse> responseObserver) {
            asyncUnimplementedUnaryCall(METHOD_GET_USER, responseObserver);
        }

        /**
         *
         */
        public void listUser(ListUserInfoRequest request,
                             io.grpc.stub.StreamObserver<ListUserInfoResponse> responseObserver) {
            asyncUnimplementedUnaryCall(METHOD_LIST_USER, responseObserver);
        }

        @Override
        public final io.grpc.ServerServiceDefinition bindService() {
            return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
                    .addMethod(
                            METHOD_GET_USER,
                            asyncUnaryCall(
                                    new MethodHandlers<
                                            GetUserInfoRequest,
                                            GetUserInfoResponse>(
                                            this, METHODID_GET_USER)))
                    .addMethod(
                            METHOD_LIST_USER,
                            asyncUnaryCall(
                                    new MethodHandlers<
                                            ListUserInfoRequest,
                                            ListUserInfoResponse>(
                                            this, METHODID_LIST_USER)))
                    .build();
        }
    }

    /**
     * <pre>
     * 服务接口定义，服务端和客户端都要遵守该接口进行通信
     * </pre>
     */
    public static final class RPCUserServiceStub extends io.grpc.stub.AbstractStub<RPCUserServiceStub> {
        private RPCUserServiceStub(io.grpc.Channel channel) {
            super(channel);
        }

        private RPCUserServiceStub(io.grpc.Channel channel,
                                   io.grpc.CallOptions callOptions) {
            super(channel, callOptions);
        }

        @Override
        protected RPCUserServiceStub build(io.grpc.Channel channel,
                                           io.grpc.CallOptions callOptions) {
            return new RPCUserServiceStub(channel, callOptions);
        }

        /**
         *
         */
        public void getUser(GetUserInfoRequest request,
                            io.grpc.stub.StreamObserver<GetUserInfoResponse> responseObserver) {
            asyncUnaryCall(
                    getChannel().newCall(METHOD_GET_USER, getCallOptions()), request, responseObserver);
        }

        /**
         *
         */
        public void listUser(ListUserInfoRequest request,
                             io.grpc.stub.StreamObserver<ListUserInfoResponse> responseObserver) {
            asyncUnaryCall(
                    getChannel().newCall(METHOD_LIST_USER, getCallOptions()), request, responseObserver);
        }
    }

    /**
     * <pre>
     * 服务接口定义，服务端和客户端都要遵守该接口进行通信
     * </pre>
     */
    public static final class RPCUserServiceBlockingStub extends io.grpc.stub.AbstractStub<RPCUserServiceBlockingStub> {
        private RPCUserServiceBlockingStub(io.grpc.Channel channel) {
            super(channel);
        }

        private RPCUserServiceBlockingStub(io.grpc.Channel channel,
                                           io.grpc.CallOptions callOptions) {
            super(channel, callOptions);
        }

        @Override
        protected RPCUserServiceBlockingStub build(io.grpc.Channel channel,
                                                   io.grpc.CallOptions callOptions) {
            return new RPCUserServiceBlockingStub(channel, callOptions);
        }

        /**
         *
         */
        public GetUserInfoResponse getUser(GetUserInfoRequest request) {
            return blockingUnaryCall(
                    getChannel(), METHOD_GET_USER, getCallOptions(), request);
        }

        /**
         *
         */
        public ListUserInfoResponse listUser(ListUserInfoRequest request) {
            return blockingUnaryCall(
                    getChannel(), METHOD_LIST_USER, getCallOptions(), request);
        }
    }

    /**
     * <pre>
     * 服务接口定义，服务端和客户端都要遵守该接口进行通信
     * </pre>
     */
    public static final class RPCUserServiceFutureStub extends io.grpc.stub.AbstractStub<RPCUserServiceFutureStub> {
        private RPCUserServiceFutureStub(io.grpc.Channel channel) {
            super(channel);
        }

        private RPCUserServiceFutureStub(io.grpc.Channel channel,
                                         io.grpc.CallOptions callOptions) {
            super(channel, callOptions);
        }

        @Override
        protected RPCUserServiceFutureStub build(io.grpc.Channel channel,
                                                 io.grpc.CallOptions callOptions) {
            return new RPCUserServiceFutureStub(channel, callOptions);
        }

        /**
         *
         */
        public com.google.common.util.concurrent.ListenableFuture<GetUserInfoResponse> getUser(
                GetUserInfoRequest request) {
            return futureUnaryCall(
                    getChannel().newCall(METHOD_GET_USER, getCallOptions()), request);
        }

        /**
         *
         */
        public com.google.common.util.concurrent.ListenableFuture<ListUserInfoResponse> listUser(
                ListUserInfoRequest request) {
            return futureUnaryCall(
                    getChannel().newCall(METHOD_LIST_USER, getCallOptions()), request);
        }
    }

    private static final class MethodHandlers<Req, Resp> implements
            io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
            io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
            io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
            io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
        private final RPCUserServiceImplBase serviceImpl;
        private final int methodId;

        MethodHandlers(RPCUserServiceImplBase serviceImpl, int methodId) {
            this.serviceImpl = serviceImpl;
            this.methodId = methodId;
        }

        @Override
        @SuppressWarnings("unchecked")
        public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
            switch (methodId) {
                case METHODID_GET_USER:
                    serviceImpl.getUser((GetUserInfoRequest) request,
                            (io.grpc.stub.StreamObserver<GetUserInfoResponse>) responseObserver);
                    break;
                case METHODID_LIST_USER:
                    serviceImpl.listUser((ListUserInfoRequest) request,
                            (io.grpc.stub.StreamObserver<ListUserInfoResponse>) responseObserver);
                    break;
                default:
                    throw new AssertionError();
            }
        }

        @Override
        @SuppressWarnings("unchecked")
        public io.grpc.stub.StreamObserver<Req> invoke(
                io.grpc.stub.StreamObserver<Resp> responseObserver) {
            switch (methodId) {
                default:
                    throw new AssertionError();
            }
        }
    }

    private static final class RPCUserServiceDescriptorSupplier implements io.grpc.protobuf.ProtoFileDescriptorSupplier {
        @Override
        public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
            return RPCUserServiceApi.getDescriptor();
        }
    }
}
