/*
  Melody

 Plays a melody

 circuit:
 * 8-ohm speaker on digital pin 8

 created 21 Jan 2010
 modified 30 Aug 2011
 by Tom Igoe

This example code is in the public domain.

 http://www.arduino.cc/en/Tutorial/Tone

 */

#ifdef USE_TONE


#include "pitches.h"
#include <TimerFreeTone.h>
// notes in the melody:
//int melody[] = {
  //NOTE_C4, NOTE_G3, NOTE_G3, NOTE_A3, NOTE_G3, 0, NOTE_B3, NOTE_C4  
//};
/*

int melody[] = {
NOTE_G4,//5  
NOTE_G4,//5
NOTE_A4,//6
NOTE_G4,//5
NOTE_C5,//1.
NOTE_B4,//7
0,
NOTE_G4,//5
NOTE_G4,//5
NOTE_A4,//6
NOTE_G4,//5
NOTE_D5,//2.
NOTE_C5,//1.
0,
NOTE_G4,//5
NOTE_G4,//5
NOTE_G5,//5.
NOTE_E5,//3.
NOTE_C5,//1.
NOTE_B4,//7
NOTE_A4,//6
0,
NOTE_F5,//4.
NOTE_F5,//4.
NOTE_E5,//3.
NOTE_C5,//1.
NOTE_D5,//2.
NOTE_C5,//1.
0,
};


// note durations: 4 = quarter note, 8 = eighth note, etc.:
//int noteDurations[] = {
//  4, 8, 8, 4, 4, 4, 4, 4
//};
int noteDurations[] = {
  8,8,4,4,4,4,
  4,
  8,8,4,4,4,4,
  4,
  8,8,4,4,4,4,2,
  8,
  8,8,4,4,4,2,
  4,
};
*/
int melody1[] = { 262, 196, 196, 220, 196, 0, 247, 262 };
int duration1[] = { 250, 125, 125, 250, 250, 250, 250, 250 };

void Notes_play(int tone_pin) {
  for (int thisNote = 0; thisNote < 8; thisNote++) { // Loop through the notes in the array.
    TimerFreeTone(tone_pin, melody1[thisNote], duration1[thisNote]); // Play melody[thisNote] for duration[thisNote].
    delay(50); // Short delay between notes.
  }
  /*
  // iterate over the notes of the melody:
  for (int thisNote = 0; thisNote < 29; thisNote++) {

    // to calculate the note duration, take one second
    // divided by the note type.
    //e.g. quarter note = 1000 / 4, eighth note = 1000/8, etc.
    int noteDuration = 1000 / noteDurations[thisNote];
    TimerFreeTone(tone_pin, melody[thisNote], noteDuration);

    // to distinguish the notes, set a minimum time between them.
    // the note's duration + 30% seems to work well:
    int pauseBetweenNotes = noteDuration * 1.30;
     delay(pauseBetweenNotes);
  }
  */
}


#endif