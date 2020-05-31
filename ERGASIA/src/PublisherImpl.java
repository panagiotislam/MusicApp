import com.mpatric.mp3agic.*;
import impl.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;



public class PublisherImpl  {
	private final static String BROKERIP = "127.0.0.1";
	private final static int id=2;
	private final static String comma = "m";
	private static Map<String, Value > CompinedMap= new HashMap<>();
	private static Map<String, Value > CompinedMap2= new HashMap<>();


	public static void main(String[] args) {

		System.out.println("Publisher with id "+ id + " starting...");

		String directory = "dataset1";
		File mp3Dir = new File(directory);

		CompinedMap2 = Loadataset(mp3Dir);
		CompinedMap2.forEach((d,k) -> {
			//System.out.println(k.toString());
			PublisherNode publisherNode = new PublisherNode(id, d.split("-")[0], BROKERIP, 7999 + Hash.getBroker(d.split("-")[0]) , k);
			publisherNode.connect();
		});



	}




	public static Map<String, Value> Loadataset(File path) {
		try {
			File[] mp3files = path.listFiles();
			if (mp3files != null) {
				for (File p : mp3files) {
					if (p.isFile()) {
						if ((!p.toString().contains("._")) && (p.toString().endsWith("mp3"))) {
							Mp3File file = new Mp3File(p);
							if (file.hasId3v1Tag()) {

								ID3v1 tag1 = file.getId3v1Tag();
								String ArtistName = tag1.getArtist();
								//SumNames.add(ArtistName);
								if (!("".equals(ArtistName) || ArtistName == null)) {
									MusicFile music = new MusicFile(p, 1);
									ArtistName += "-" + p.getName().replace(".mp3","");
									if (id == 1) {
										if (ArtistName.substring(0, 1).compareToIgnoreCase(comma) <= 0) {
											CompinedMap.put(ArtistName, new Value(music));
										}
									}else if (id == 2){
										if (ArtistName.substring(0, 1).compareToIgnoreCase(comma) > 0) {
											CompinedMap.put(ArtistName, new Value(music));
										}
									}
								}else {
									if (id==2){
										MusicFile music = new MusicFile(p, 1);
										music.setArtistName("Unknown"+"-" + p.getName().replace(".mp3",""));
										CompinedMap.put("Unknown", new Value(music));
									}
								}
							} else if (file.hasId3v2Tag()){

								ID3v2 tag2 = file.getId3v2Tag();
								String ArtistName = tag2.getArtist();
								//SumNames.add(ArtistName);
								if (!("".equals(ArtistName) || ArtistName == null)) {
									MusicFile music = new MusicFile(p, 2);
									ArtistName+="-"+p.getName().replace(".mp3","");
									if (id == 1) {
										if (ArtistName.substring(0, 1).compareToIgnoreCase(comma) <= 0) {
											CompinedMap.put(ArtistName, new Value(music));
										}
									}else if (id == 2){
										if (ArtistName.substring(0, 1).compareToIgnoreCase(comma) > 0) {
											CompinedMap.put(ArtistName, new Value(music));

										}
									}
								}else {
									if(id==2){
										MusicFile music = new MusicFile(p, 2);
										music.setArtistName("Unknown"+"-" + p.getName().replace(".mp3",""));
										CompinedMap.put("Unknown", new Value(music));
									}
								}
							}
						}
					} else if (p.isDirectory()) {

						Loadataset(p);
					}
				}
			} else {
				System.out.println("Couldn't find any mp3.");
			}
		} catch (UnsupportedTagException | IOException | InvalidDataException e) {
			e.printStackTrace();
		}
		return CompinedMap;
	}

}
