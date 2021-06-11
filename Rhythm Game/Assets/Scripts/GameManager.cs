using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class GameManager : MonoBehaviour
{
	public AudioSource theMusic;

	public bool startPlaying;

	public BeatScroller theBS;

    public static GameManager instance;

    public int currentScore;

    public int scorePerNote = 100;
    public Text scoreText;
    public Text multiText;
    public Text accText;
    public float totalNotes;
    public float totalNotesHit;
    public float accuracy;
    // Start is called before the first frame update
    void Start()
    {
        instance = this;
        scoreText.text = "Score: 0";
        accuracy = 0;
        totalNotes = 0;
        accText.text = "Accuracy: 0%";
    }

    // Update is called once per frame
    void Update()
    {
        if(!startPlaying)
        {
        	if(Input.anyKeyDown)
        	{
        		startPlaying = true;
        		theBS.hasStarted = true;

        		theMusic.Play();
        	}
        }
    }
    public void NoteHit()
    {
        Debug.Log("Hit On Time");
        currentScore += scorePerNote;
        scoreText.text = "Score: " + currentScore;
        totalNotes += 1;
        totalNotesHit += 1;
        accuracy = (totalNotesHit / totalNotes) * 100;
        accText.text = "Accuracy: " + accuracy + "%";

    }
    public void NoteMissed()
    {
        Debug.Log("Missed Note");
        currentScore -= 10;
        scoreText.text = "Score: " + currentScore;
        totalNotes += 1;
        accuracy = (totalNotesHit / totalNotes) * 100;
        accText.text = "Accuracy: " + accuracy + "%";
    }
}
