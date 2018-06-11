import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import java.io.File;
import java.io.RandomAccessFile;

public class Client {

    public static void main(String[] args) {

        Bootstrap b = new Bootstrap();
        b.group(new NioEventLoopGroup()).channel(NioSocketChannel.class);
        b.option(ChannelOption.TCP_NODELAY, true);
        b.handler(new ChannelInitializer<Channel>() {
            @Override
            protected void initChannel(Channel ch) throws Exception {
                ch.pipeline().addLast(new ObjectEncoder());
                ch.pipeline().addLast(new ObjectDecoder(ClassResolvers.weakCachingConcurrentResolver(null)));
                ch.pipeline().addLast(new FileUploadClientHandler());
            }
        });

        try {
            b.connect("127.0.0.1",6666).sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }
}



class FileUploadClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        File file = new File("D:\\aaaa.mp4");
        if (file.exists()) {
            System.out.println("开始发送...");
            DataBean dataBean = new DataBean();
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
            randomAccessFile.seek(0);//从开始位置开始读
            long length = randomAccessFile.length();
            System.out.println("文件长度:" + length);
            byte[] bytes = new byte[(int) length];
            randomAccessFile.read(bytes);//一次读完
            dataBean.setData(bytes);
            ctx.channel().writeAndFlush(dataBean);
            System.out.println("发送完成..");
        } else {
            System.out.println(file.getCanonicalFile() + "不存在");
        }

    }

}