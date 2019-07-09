Some of the undergraduate assingments and projects from my studies.

Jukebox usage: (The server uses port 40000 to accept clients!)

1, First start the MusicBoxServer then the Client
2, After the music starts you can use several commands:
  - add <title>: You can add your own music to the server. After using the command with your song's title, the next line should be the notes with their length in eights just so: C 4 E 4 C 4 E 4 G 8 G 8 REP 6;1... As you can see there is special "command note" for repetition which says that in this case the last 6 notes should be repeated once. There's also a command note for pause which is R.
  - play <tempo> <transpose> <title>: Play a certain song(title) with a set tempo (125 is normal), transpose the song by half-steps up or down (+-<number>).
  - change <id> <tempo> <transpose>: Change the parameters of currently playing songs.
  - stop <id>: Stop the song with id: <id>.
