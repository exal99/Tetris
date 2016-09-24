package highscore;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Map.Entry;

public class HighScore implements Serializable{
	private static final long serialVersionUID = -7008171349896583459L;
	private Hashtable<Long, String> highScore;
	
	public HighScore() {
		highScore = new Hashtable<Long, String>();
	}
	
	public void addScore(String name, long score) {
		highScore.put(score, name);
	}
	
	public ArrayList<Entry<String, Long>> getHighScorer() {
		ArrayList<Long> list = new ArrayList<Long>(highScore.keySet());
		Collections.sort(list, Collections.reverseOrder());
		ArrayList<Entry<String, Long>> scores = new ArrayList<Entry<String, Long>>();
		for (Long score : list) {
			scores.add(new ScoreMap(highScore.get(score), score));
		}
		return scores;
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<Entry<String, Long>> getPartOfList(int numValues) {
		Entry<String,Long>[] array = (Entry<String, Long>[]) Arrays.copyOfRange(getHighScorer().toArray(), 0, numValues);
		return new ArrayList<Entry<String,Long>>(Arrays.asList(array));
	}
	
	public static HighScore getScore() {
		HighScore score = null;
		try {
			InputStream in = new FileInputStream("high_score.score");
			InputStream buffer = new BufferedInputStream(in);
			ObjectInput object = new ObjectInputStream(buffer);
			score = (HighScore) object.readObject();
			object.close();
		} catch (FileNotFoundException e1) {
			System.out.println("File not found");
			score = new HighScore();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return score;
	}
	
	private class ScoreMap implements Entry<String, Long> {
		
		private String name;
		private Long score;
		
		public ScoreMap(String s, Long l) {
			name = s;
			score = l;
		}

		@Override
		public String getKey() {
			return name;
		}

		@Override
		public Long getValue() {
			return score;
		}

		@Override
		public Long setValue(Long value) {
			Long temp = score;
			score = value;
			return temp;
		}
		
		@Override
		public String toString() {
			return name + ": " + score;
		}
	}
}
