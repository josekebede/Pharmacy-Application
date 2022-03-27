const express = require("express");
const fs = require("fs");
const bcrypt = require("bcrypt");

const app = express();

app.use(function (req, res, next) {
    res.header("Access-Control-Allow-Origin", "*");
    res.header(
        "Access-Control-Allow-Headers",
        "Origin, X-Requested-With, Content-Type, Accept"
    );
    next();
});
app.use(express.urlencoded({ extended: false }));
app.use(express.json());
app.post("/auth", (req, res) => {
    try {
        const data = fs.readFileSync("./database/users.txt", "utf8");
        const userInfo = JSON.parse(data);

        checkLoginInfo(
            req.body.username,
            req.body.password,
            userInfo.users
        ).then((isCorrect) => {
            if (isCorrect) {
                res.status(200).send("Found");
            } else {
                res.status(250).send("Not Found");
            }
        });
    } catch (err) {
        console.error(err);
    }
});

async function checkLoginInfo(username, plaintextPassword, users) {
    for (var i = 0; i < users.length; i++) {
        const result = await bcrypt.compare(
            plaintextPassword,
            users[i].password
        );
        if (result && username == users[i].username) return result;
    }
    return false;
}
app.get("/get", (req, res) => {
    res.status(200).send("works");
});

app.get("/medicine", (req, res) => {
    const medicine = JSON.parse(
        fs.readFileSync("./database/medcineInfo.json", "utf8")
    );
    res.status(200).send(medicine.medicine);
});

app.post("/addMedicine", (req, res) => {
    const medcineName = req.body.name;
    const medDescription = req.body.description;
    const medStock = req.body.stock;

    // Checks if proper information is provided
    if (
        medcineName == undefined ||
        medcineName == null ||
        medDescription == undefined ||
        medDescription == null ||
        medStock == undefined ||
        medStock == null
    ) {
        res.status(400).send("Proper information not supplied");
        return;
    }
    
    var medObject = {
        name: medcineName,
        Description: medDescription,
        stock: medStock,
    };

    
    
    const medList = JSON.parse(
        fs.readFileSync("./database/medcineInfo.json", "utf8")
    );
    
    //#region Duplicate are handled here 
    var foundDuplicate = false;
    
    // Traverses through the medicine list and checks for duplicates.
    // If it finds duplicates, it replaces the medicine description and stock
    for(var i = 0; i < medList.medicine.length && !foundDuplicate; i++) {
        if(medList.medicine[i].name == medObject.name){
            foundDuplicate = true;
            medList.medicine[i].Description = medObject.Description;
            medList.medicine[i].stock = medObject.stock;
        }
    }
    // If a duplicate is found, it is saved and the proper information is sent to the client
    if(foundDuplicate) {
        fs.writeFile("./database/medcineInfo.json",JSON.stringify(medList), (err) => {
            if(err)
                res.status(400).send("Medicine not updated");
            else
                res.status(200).send(medObject.name + " with it's information has been updated");
        });
        return;
    }
    //#endregion

    medList.medicine.push(medObject); // Pushes new object
    medList.medicine.sort(comparisionFunction); // Sorts the objects

    // Saves to file
    fs.writeFile("./database/medcineInfo.json",JSON.stringify(medList), (err) => {
        if(err)
            res.status(400).send("Medicine not added");
        else
            res.status(200).send(medObject.name + " with it's information has been added");
    });
});

function comparisionFunction(a, b) {
    if(a.name.toLowerCase() < b.name.toLowerCase())
        return -1;
    else if (a.name.toLowerCase() > b.name.toLowerCase())
        return 1;
    else 
        return 0;
}

app.post("/deleteMedicine", (req, res) => {
    const medicineName = req.body.name;
    if(medicineName == undefined) {
        res.status(404).send("Name not defined");
        return;
    }
    const medicineList = JSON.parse(
        fs.readFileSync("./database/medcineInfo.json", "utf8")
    );

    medicineList.medicine = medicineList.medicine.filter((value, index, arr) => {
        return value.name != medicineName;
    })

    fs.writeFile("./database/medcineInfo.json",JSON.stringify(medicineList), (err) => {
        if(err)
            res.status(400).send("Medicine not deleted");
        else
            res.status(200).send(medicineName + " is deleted");
    });
    return;
    
});


app.listen(8000, (err) => {
    if (err) {
        console.error(err);
    } else {
        console.log("Listening on Port 8000");
    }
});
