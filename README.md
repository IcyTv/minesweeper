# Minesweeper

This is the classic minesweeper in Java, but with a twist: it has multiplayer! Just enter the same code as your friend, choose different names and enjoy minesweeper together.

## Playing <br/>
You can use the deployed app on herokuapp.com. To use this version, just download the binary .exe or .jar file from [here](https://github.com/IcyTv/minesweeper/releases).<br/><br/>
To decrease lag, you can host the server locally. Clone the repository, import it straight into [Eclipse](http://www.eclipse.org/) and change the `BASE_IP` in the src/requests/Request.java file to your local IP. Open a console at the basepath of the project (making sure that you have [nodejs](https://nodejs.org) and [npm](https://www.npmjs.com/) installed) and run `npm install` and then `npm start`. Then run the project with Eclipse.
