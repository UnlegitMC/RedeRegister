package me.method17.func;

import com.github.steveice10.mc.protocol.MinecraftConstants;
import com.github.steveice10.mc.protocol.MinecraftProtocol;
import com.github.steveice10.mc.protocol.data.game.window.ClickItemParam;
import com.github.steveice10.mc.protocol.data.game.window.WindowAction;
import com.github.steveice10.mc.protocol.data.message.Message;
import com.github.steveice10.mc.protocol.packet.MinecraftPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.ClientChatPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.window.ClientWindowActionPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerTitlePacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.window.ServerSetSlotPacket;
import com.github.steveice10.opennbt.tag.builtin.CompoundTag;
import com.github.steveice10.packetlib.Session;
import com.github.steveice10.packetlib.event.session.DisconnectedEvent;
import com.github.steveice10.packetlib.event.session.PacketReceivedEvent;
import com.github.steveice10.packetlib.event.session.SessionAdapter;
import com.github.steveice10.packetlib.tcp.TcpSessionFactory;
import me.method17.Reg;

import java.net.Proxy;

public class Client {
    private boolean canCheckClique=true;

	public Client(String name,String password){
        MinecraftProtocol protocol = new MinecraftProtocol(name);

        final com.github.steveice10.packetlib.Client client = new com.github.steveice10.packetlib.Client("redesky.com", 25565, protocol, new TcpSessionFactory(Proxy.NO_PROXY));
        client.getSession().setFlag(MinecraftConstants.AUTH_PROXY_KEY, Proxy.NO_PROXY);

        client.getSession().addListener(new SessionAdapter() {
            @Override
            public void packetReceived(PacketReceivedEvent event) {
                MinecraftPacket packet=event.getPacket();
                Session session=event.getSession();
                //bypass antibot
                if(packet instanceof ServerSetSlotPacket){
                    ServerSetSlotPacket setSlotPacket=(ServerSetSlotPacket) packet;
                    //null check
                    if(setSlotPacket.getItem()!=null&&canCheckClique){
                        try {
                            CompoundTag tag=setSlotPacket.getItem().getNBT().get("display");
                            String value= (String) tag.get("Name").getValue();
                            if(value.contains("aqui")){
                                session.send(new ClientWindowActionPacket(setSlotPacket.getWindowId(),114514,setSlotPacket.getSlot(),
                                        setSlotPacket.getItem(), WindowAction.CLICK_ITEM, ClickItemParam.LEFT_CLICK));
                                Reg.getLogger().info("try click item(id="+setSlotPacket.getWindowId()+", slot="+setSlotPacket.getSlot()+")");
                                canCheckClique=false;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }else if(packet instanceof ServerTitlePacket){
                    //do login
                    ServerTitlePacket titlePacket=(ServerTitlePacket) packet;
                    Message message=titlePacket.getSubtitle();
                    if(message!=null){
                        String title=message.getText();
                        Reg.getLogger().warn(title);
                        if(title.contains("/login")){
                            Reg.getLogger().warn(name+" got registered :sadface:");
                            client.getSession().disconnect("LOL");
                        }else{
                            session.send(new ClientChatPacket("/register "+password+" "+password));
                            Reg.getRegisterLog().addAccount(name,password);
                            Reg.getLogger().info(name+" register complete!");
                        }
                    }
                }
            }

            @Override
            public void disconnected(DisconnectedEvent event) {
                Reg.getLogger().warn("Disconnected: " + Message.fromString(event.getReason()).getFullText());
            }
        });

        Reg.getLogger().warn("connecting to server(id="+name+", passwd="+password+")");
        client.getSession().connect();
        Reg.getLogger().warn("connected(id="+name+", passwd="+password+")");
	}
}
