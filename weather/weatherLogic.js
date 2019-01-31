const fs = require("fs");
const readline = require("readline");

const rl = readline.createInterface({
  input: process.stdin,
  output: process.stdout
});

console.log("Input File Name: (Example.txt)");

rl.on("line", fileName => {
  convertXmlToJson(fileName);
});

const convertXmlToJson = fileName => {
    fs.readFile(__dirname + "/" + fileName, (err, data) => {
        if (err) {
          console.log(err);
        } else {
            let jsonWeather = "";
            let depth = 0;
            const lines = data.toString().split("\n");
            for(const line of lines) {
                if(line.indexOf("?xml") !== -1) {
                    continue;
                } else if(line.indexOf("current") !== -1) {
                    if(line.indexOf('/') === -1 ){
                        jsonWeather += "{\n"
                        depth++;
                        continue;
                    } else {
                        jsonWeather += "}"
                        continue;
                    }
                }
                const splittedLine = line.trim().split('"');
                if(splittedLine.length === 1) {
                    if(line.indexOf('<') !== line.lastIndexOf('<')) {
                        const key = line.substring(line.indexOf('<') + 1, line.indexOf('>'))
                        const value = line.substring(line.indexOf('>') + 1, line.lastIndexOf('<'))
                        for(let i=0; i<depth; i++){
                            jsonWeather += "    "
                        }
                        jsonWeather += `"${key}": "${value}",\n`
                    } else if(line.indexOf('/') !== -1) {
                        depth--
                        for(let i = 0; i < depth; i++){
                            jsonWeather += "    "
                        }
                        jsonWeather += `},\n`
                    } else {
                        const key = line.substring(line.indexOf('<') + 1, line.indexOf('>'))
                        for(let i=0; i<depth; i++){
                            jsonWeather += "    "
                        }
                        jsonWeather += `"${key}": {\n`
                        depth++
                    }
                } else {
                    const key = splittedLine[0].substring(splittedLine[0].indexOf('<') + 1, splittedLine[0].indexOf(" "))
                        for(let i=0; i<depth; i++){
                            jsonWeather += "    "
                        }
                        jsonWeather += `"${key}": {\n`
                        depth++
                        const keysAndValues = {};
                        for(let i = 1; i < splittedLine.length; i+=2) {
                            const miniKey = splittedLine[i-1].substring(splittedLine[i-1].indexOf(" ") + 1, splittedLine[i-1].indexOf('='))
                            const miniValue = splittedLine[i]
                            for(let i=0; i<depth; i++){
                                jsonWeather += "    "
                            }
                            jsonWeather += `"${miniKey}": "${miniValue}",\n`
                        }
                    if(line.indexOf('/') !== -1){
                        depth--
                        for(let i=0; i<depth; i++){
                            jsonWeather += "    "
                        }
                        jsonWeather += `},\n`
                    }
                }
            }
            fs.writeFile(
                __dirname + "/" + fileName.substring(0, fileName.indexOf(".")) + ".json",
                jsonWeather,
                () => {
                  console.log("File Converter from XML to JSON: Success!");
                  process.stdin.destroy();
                }
              );
        }
    })
}
