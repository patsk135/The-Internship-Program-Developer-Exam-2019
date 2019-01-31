const fs = require("fs");
const readline = require("readline");

const rl = readline.createInterface({
  input: process.stdin,
  output: process.stdout
});

console.log("Input File Name: (Example.txt)");

rl.on("line", line => {
  convertXmlToJson(line);
});

const convertXmlToJson = fileName => {
  fs.readFile(__dirname + "/" + fileName, (err, data) => {
    if (err) {
      console.log(err);
    }
    const s = data.toString();
    const pattern = /"([^"]*)"/g;
    const o = s.match(pattern);
    const country = s.substring(
      s.indexOf("<country>") + 9,
      s.indexOf("</country>")
    );
    const jsonWeather = 
`{
  "city": {
    "id": ${o[2]},
    "name": ${o[3]},
    "coord": {
      "lon": ${o[4]},
      "lat": ${o[5]}
    },
    "country": "${country}",
    "sun": {
      "rise": ${o[6]},
      "set": ${o[7]}
    }
  },
  "temperature": {
    "value": ${o[8]},
    "min": ${o[9]},
    "max": ${o[10]},
    "unit": ${o[11]}
  },
  "humidity": {
    "value": ${o[12]},
    "unit": ${o[13]}
  },
  "pressure": {
    "value": ${o[14]},
    "unit": ${o[15]}
  },
  "wind": {
    "speed": {
      "value": ${o[16]},
      "name": ${o[17]}
    },
    "direction": {
      "value": ${o[18]},
      "code": ${o[19]},
      "name": ${o[20]}
    }
  },
  "clouds": {
    "value": ${o[21]},
    "name": ${o[22]}
  },
  "visibility": {
    "value": ${o[23]}
  },
  "precipitation": {
    "mode": ${o[24]}
  },
  "weather": {
    "number": ${o[25]},
    "value": ${o[26]},
    "icon": ${o[27]}
  },
  "lastupdate": {
    "value": ${o[28]}
  }
}`;
    fs.writeFile(
      __dirname + "/" + fileName.substring(0, fileName.indexOf(".")) + ".json",
      jsonWeather,
      () => {
        console.log("File Converter from XML to JSON: Success!");
        process.stdin.destroy();
      }
    );
  });
};
