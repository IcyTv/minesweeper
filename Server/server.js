const express = require('express');
const app = express();

let games = {};

app.use(express.json());

app.use("/register/:code/:user", (req, res) => {
  if (games[req.params.code] != undefined && games[req.params.code].users.length <= 2) {
    console.log("Second player in game " + req.params.code);
    games[req.params.code].users.push(req.params.user);
    console.log(games[req.params.code].users);
  } else if (games[req.params.code] == undefined) {
    console.log("New game registered!");
    games[req.params.code] = {
      users: [req.params.user],
      currentPlayer: 0,
      map: undefined,
      gotMap: true,
      dead: "",
      requestRestart: [],
      restart:false
    }
  } else {
    res.statusCode = 403;
    res.end("Access denied!");
  }
  res.end("Success");
});

app.use("/unregister/:code/:user", (req, res) => {
  if (games[req.params.code] == undefined || games[req.params.code].users.indexOf(req.params.user) == -1) {
    res.statusCode = 404;
    console.log("Error, indexof = " + games[req.params.code].users.indexOf(req.params.user));
    res.end("Not found!");
  } else {
    games[req.params.code].users.splice(games[req.params.code].users.indexOf(req.params.user), 1);
    if (games[req.params.code].users.length == 0) {
      console.log("Deleted game " + req.params.code);
      delete games[req.params.code];
    } else {
      console.log("Length: " + games[req.params.code].users.length);
    }
    res.end("Success");
  }
});

app.use("/status/:code", (req, res) => {
  let status = {
    player: "",
    full:false,
    turn:false,
    wait:false,
    dead:false,
    requestRestart: false,
    restart: false
  };
  if (games[req.params.code] == undefined) {
    res.statusCode = 404;
    res.end("Not found!");
    return;
  } else if(games[req.params.code].users.length < 2) {

  }else {
      status.full = true;
      let cG = games[req.params.code];
      status.player = cG.users[cG.users.indexOf(req.query.user) == 0 ? 1: 0];
      //console.log(req.query.user + " opp " + status.player);
      if(cG.dead != "" && cG.gotMap){
        status.dead = true;
      }
      if(cG.requestRestart.length >= 1 && cG.requestRestart.indexOf(req.query.user) == -1){
        status.requestRestart = true;
      }
      if(cG.restart){
        status.restart = true;
      }
      if (cG.users[cG.currentPlayer] == req.query.user && cG.gotMap) {
        status.turn = true;
      } else {
        status.wait = true;
      }
  }
  res.send(status);
});

app.use("/died/:code/:user", (req, res) => {
  if (games[req.params.code] == undefined) {
    res.statusCode = 404;
    res.end("Not found!");
  } else {
    games[req.params.code].dead = req.params.user;
    games[req.params.code].gotMap = false;
    console.log("User died: " + req.params.user)
    res.end("Success");
  }
});

app.use("/restart/:code/:user", (req, res) => {
  if (games[req.params.code] == undefined) {
    res.statusCode = 404;
    res.end("Not found!");
  } else if (games[req.params.code].requestRestart.length < 2 && games[req.params.code].requestRestart.indexOf(req.params.user) == -1){
    games[req.params.code].requestRestart.push(req.params.user);
  }
  if(games[req.params.code].requestRestart.length == 2) {
    games[req.params.code].restart = true;
    games[req.params.code].requestRestart = [];
    games[req.params.code].gotMap = false;
    games[req.params.code].dead = "";
    console.log("Restarting");
    res.end("Restarting");
  } else {
    console.log("User " + req.params.user + " requested a restart");
    res.end("Success");
  }
});

app.post("/updateMap/:code/:user", (req, res) => {
  if (games[req.params.code] == undefined) {
    res.statusCode = 404;
    res.end();
    return;
  }
  if(games[req.params.code].map == undefined){
    console.log("Registering map");
  }
  if (games[req.params.code].users[games[req.params.code].currentPlayer] == req.params.user || games[req.params.code].restart) {
    games[req.params.code].map = req.body.map;
    console.log(req.params.user + " made a move");
  }

  if(games[req.params.code].restart){
    games[req.params.code].restart = false;
    games[req.params.code].currentPlayer--;
  }

  games[req.params.code].gotMap = false;
  games[req.params.code].currentPlayer += 1;
  games[req.params.code].currentPlayer %= 2;
  res.end("Success");
});

app.use("/getMap/:code/:user", (req, res) => {
  if (games[req.params.code] == undefined) {
    res.statusCode = 404;
    res.end();
    return;
  }
  res.send({
    "map": games[req.params.code].map
  });
  if(games[req.params.code].users[games[req.params.code].currentPlayer] == req.params.user){
    games[req.params.code].gotMap = true;
  }
  //console.log("Update");
  res.end();
});

app.listen(process.env.PORT || 8000, () => {
  console.log("Server started");
});
