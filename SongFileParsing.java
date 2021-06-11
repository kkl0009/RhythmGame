/*
 * Created by Kollin Labowski as a demonstration of a possible way to read and write to files for songs in the 
 * rhythm game to make with Unity (Tyler Riddle and Nader Najjar are the other project members)
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class SongFileParsing
{
	//Main method
	public static void main(String[] args)
	{
		//This is a Song object, which will be how the computer interprets the song as its being played
		Song song = readSongFile("mySong.txt");
		//Printing it out to show how it looks
		System.out.println(song);
		
		//A separate process used to create a new song file, basically in the opposite process as before
		createSong();
	}
	
	/*
	 * This will write the necessary information to find a song to a file so that it can be stored and accessed
	 * whenever a player chooses to play a certain song
	 */
	public static void writeSongFile(String fileName, String songName, int tempo, List<Integer> beats)
	{
		try
		{
			File songFile = new File(fileName);
			songFile.createNewFile();
			FileWriter songWriter = new FileWriter(fileName);
			songWriter.write(songName + " " + tempo);
			while(!beats.isEmpty())
			{
				songWriter.write(" " + beats.get(0));
				beats.remove(0);
			}
			songWriter.close();
		}
		catch(IOException e)
		{
			System.out.println("F in the chat, file not found");
		}
	}
	
	/*
	 * This is an example of a song creation process, currently taking place entirely within command line,
	 * however this will be translated into Unity and updated as part of the final game
	 */
	public static void createSong()
	{
		String response;
		Scanner user = new Scanner(System.in);
		System.out.println("Would you like to create a new song? Yes (y) or No (n)");
		response = user.nextLine();
		if(response.contains("n"))
		{
			System.out.println("Exiting song creation...");
			user.close();
			return;
		}
		else if(!response.contains("y"))
		{
			System.out.println("Invalid response recieved...");
			user.close();
			createSong();
			return;
		}
		System.out.println("What is the name of your song?");
		response = user.nextLine();
		String songName = response.replace(" ", "|");
			
		System.out.println("What is the tempo of your song? (Just input the number)");
		int tempo = user.nextInt();
		
		System.out.println("You will now be able to enter in music for each beat,\nin order to do this, enter in all notes you would like to have in a beat\nusing 'l' for left, 'd' for down, 'u' for up, 'r' for right, and 't' to indicate a trailing note (leave blank for no notes),\nenter the value 'x' at any point to finish adding notes, and make sure to hit enter between each beat.\nNow ready to receive input:");
		
		user.nextLine();//Weird, seemingly harmless glitch that was kind of fixed with this line, but could cause an error
		
		String beat = user.nextLine();
		List<Integer> beats = new LinkedList<Integer>();
		while(!beat.contains("x"))
		{
			int value = 0;
			String notify = "";
			if(beat.contains("t"))
			{
				value += 16;
				notify += "t";
			}
			if(beat.contains("l"))
			{
				value += 8;
				notify += "l";
			}
			if(beat.contains("d"))
			{
				value += 4;
				notify += "d";
			}
			if(beat.contains("u"))
			{
				value += 2;
				notify += "u";
			}
			if(beat.contains("r"))
			{
				value += 1;
				notify += "r";
			}
			
			if(notify.isEmpty())
				System.out.println("Adding empty beat");
			else
				System.out.println("Adding values: " + notify);
			
			beats.add(value);
			beat = user.nextLine();
		}
		
		System.out.println("Exiting song edit mode...");
		System.out.println("What would you like to name the file containing this song?");
		String fileName = user.nextLine();
		writeSongFile(fileName, songName, tempo, beats);
		System.out.println("Your song was successfully written to a file called " + fileName);
		System.out.println("Now exiting song creation...");
		user.close();
	}
	
	/*
	 * Given a song file in the format above, this will read the file and interpret it.
	 * Interpretting it in this case means creating a new Song object that the computer
	 * can use when the game is actively being played (this function will run when the 
	 * song is loading)
	 */
	public static Song readSongFile(String fileName)
	{
		String songName = "";
		int tempo = 0;
		List<Integer> songNotes = new LinkedList<Integer>();
		Song song = new Song();
		
		try
		{
			File songFile = new File(fileName);
			Scanner songScanner = new Scanner(songFile);
			
			songName = songScanner.next();
			songName = songName.replace("|", " ");
			
			tempo = songScanner.nextInt();
			
			while(songScanner.hasNextInt())
			{
				songNotes.add(songScanner.nextInt());
			}
			
			songScanner.close();
			
			song = new Song(songName, tempo, songNotes);
		}
		catch(FileNotFoundException e)
		{
			System.out.println("File with the given name was not found");
		}
		
		return song;
	}
	
	/*
	 * The Song class here is used to encapsulate important details the game will use as a song
	 * is actively being selected and played by a user of the game. It encapsulates the name and
	 * tempo of the song, as well as what notes are present in each beat
	 */
	private static class Song
	{
		private String name;
		private int tempo;
		private List<boolean[]> notes;
		
		public Song()
		{
			name = "";
			tempo = 0;
			this.notes = new LinkedList<boolean[]>();
		}
		
		public Song(String name, int tempo, List<Integer> notes)
		{
			this.name = name;
			this.tempo = tempo;
			this.notes = interpretList(notes);
		}
		
		/*
		 * Most accessor and mutator methods are not actually used but are there in case they are
		 * needed eventually
		 */
		public void updateName(String name)
		{
			this.name = name;
		}
		
		public void updateTempo(int tempo)
		{
			this.tempo = tempo;
		}
		
		public void updateNotes(List<boolean[]> notes)
		{
			this.notes = notes;
		}
		
		public String getName()
		{
			return name;
		}
		
		public int getTempo()
		{
			return tempo;
		}
		
		public List<boolean[]> getNotes()
		{
			return notes;
		}
		
		/*
		 * This toString method was added to make it easier to see what is actually contained
		 * within a Song object
		 */
		public String toString()
		{
			String output = "Name: " + name + "   Tempo: " + tempo + "   Notes:";
			for(int i = 0; i < notes.size(); i++)
			{
				output += " [ ";
				for(int j = 0; j < notes.get(i).length; j++)
				{
					output += notes.get(i)[j] + " ";
				}
				output += "] ";
			}
			return output;
		}
		
		/*
		 * This function translates the integers found within the file to arrays
		 * of booleans that the computer can use as the player is playing the song.
		 * This is not entirely necessary as the computation is not very expensive
		 * but it should make it easier to implement everything in Unity hopefully
		 */
		private List<boolean[]> interpretList(List<Integer> integerNotes)
		{
			List<boolean[]> noteMeanings = new LinkedList<boolean[]>();
			
			while(!integerNotes.isEmpty())
			{
				boolean[] noteValues = new boolean[5];
				for(int i = 0; i < noteValues.length; i++)
				{
					int check = (int)Math.pow(2, noteValues.length - i - 1);
					noteValues[i] = ((integerNotes.get(0) & check) == check);
				}
				noteMeanings.add(noteValues);
				
				integerNotes.remove(0);
			}
			
			return noteMeanings;
		}
	}
}