package highscore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Map.Entry;

public class HighScore implements Serializable{
	private static final long serialVersionUID = 1L;
	private Hashtable<Long, String> highScore;
	
	public HighScore() {
		highScore = new Hashtable<Long, String>();
	}
	
	public void addScore(String name, long score) {
		highScore.put(score, name);
	}
	
	public ArrayList<Entry<String, Long>> getHighScorer() {
		ArrayList<Long> list = new ArrayList<Long>(highScore.keySet());
		Collections.sort(list);
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
