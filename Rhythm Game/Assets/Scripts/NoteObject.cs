using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class NoteObject : MonoBehaviour
{
	public bool canBePressed;

	public KeyCode keyToPress;
    public KeyCode keyToPressTwo;

    public bool cleared;
    // Start is called before the first frame update
    void Start()
    {
        cleared = false;
    }

    // Update is called once per frame
    void Update()
    {
        if(Input.GetKeyDown(keyToPress) || Input.GetKeyDown(keyToPressTwo))
        {
        	if(canBePressed)
        	{
                GameManager.instance.NoteHit();
                cleared = true;
                gameObject.SetActive(false);
                
        	}
        }
    }

    private void OnTriggerEnter2D(Collider2D other)
    {
    	if(other.tag == "Activator")
    	{
    		canBePressed = true;
    	}
    }

    private void OnTriggerExit2D(Collider2D other)
    {
    	if(other.CompareTag("Activator"))
    	{
    		canBePressed = false;
            if(!cleared)
            {
                GameManager.instance.NoteMissed();
            }
                
        }
        
    }
}
