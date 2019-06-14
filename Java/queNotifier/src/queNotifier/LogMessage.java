package queNotifier;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogMessage{
	//^\[(\d{2}:\d{2}:\d{2})\] \[([^\]]*)\/([^/]*)\]: \[(CHAT)\]
	//[04:11:26] [main/INFO]: [CHAT] jared2013 joined the game
	final static Pattern genericMessagePattern = Pattern.compile("^\\[(\\d{2}:\\d{2}:\\d{2})\\] \\[([^\\]]*)\\/([^/]*)\\]: \\[(CHAT)\\]");
	//^\[(\d{2}:\d{2}:\d{2})\] \[([^\]]*)\/([^/]*)\]: \[(CHAT)\] <(\w+)>
	//[03:13:48] [main/INFO]: [CHAT] <Eilish_Billie> Learn the best tips & secrets of 2b2t - https://joinvo.co
	final static Pattern userChatPattern = Pattern.compile("^\\[(\\d{2}:\\d{2}:\\d{2})\\] \\[([^\\]]*)\\/([^/]*)\\]: \\[(CHAT)\\] <(\\w+)>");
	//^\[(\d{2}:\d{2}:\d{2})\] \[([^\]]*)\/([^/]*)\]: \[(CHAT)\] Position in queue:
	//[23:55:58] [main/INFO]: [CHAT] Position in queue: 816
	final static Pattern positionInQueuePattern = Pattern.compile("^\\[(\\d{2}:\\d{2}:\\d{2})\\] \\[([^\\]]*)\\/([^/]*)\\]: \\[(CHAT)\\] Position in queue:");
	//^\[(\d{2}:\d{2}:\d{2})\] \[([^\]]*)\/([^/]*)\]: \[(CHAT)\] \[SERVER\]
	//[05:12:47] [main/INFO]: [CHAT] [SERVER] Server restarting in 5 seconds...
	final static Pattern serverMessagePattern = Pattern.compile("^\\[(\\d{2}:\\d{2}:\\d{2})\\] \\[([^\\]]*)\\/([^/]*)\\]: \\[(CHAT)\\] \\[SERVER\\]");
	//^\[(\d{2}:\d{2}:\d{2})\] \[([^\]]*)\/([^/]*)\]: \[(CHAT)\] ([A-Za-z0-9_])\w+ whispers:
	//[19:59:14] [main/INFO]: [CHAT] Fagget420 whispers: shit wrong person sorry
	final static Pattern whisperChatPattern = Pattern.compile("^\\[(\\d{2}:\\d{2}:\\d{2})\\] \\[([^\\]]*)\\/([^/]*)\\]: \\[(CHAT)\\] ([A-Za-z0-9_])\\w+ whispers:");
	//Java is trash and so am I
	private long time;
	private String content;
	private Type type = Type.OTHER;
	public LogMessage(String messageInput) {
		Matcher m = genericMessagePattern.matcher(messageInput);
		if (m.find()) {
			type = Type.GENERIC;
			content = messageInput.substring(m.end()).trim();
			
			m = serverMessagePattern.matcher(messageInput);
			if(m.find()) {
				type = Type.SERVER_MESSAGE;
			}
			
			m = positionInQueuePattern.matcher(messageInput);
			if(m.find()) {
				type = Type.QUEUE;
				content = messageInput.substring(m.end()).trim();
			}
						
			m = userChatPattern.matcher(messageInput);
			if(m.find()) {
				type = Type.CHAT;
			}
			
			m = whisperChatPattern.matcher(messageInput);
			if(m.find()) {
				type = Type.WHISPER;
			}
			
		} else {
				//System.out.println("What the fuck");
				//System.out.println(messageInput);
				content = messageInput;
			
		}
		time = System.currentTimeMillis() / 1000L;
	}
	
	public String getContent() {
		return content;
	}
	
	public Type getType() {
		return type;
	}
	
	public long getTime() {
		return time;
	}
	}