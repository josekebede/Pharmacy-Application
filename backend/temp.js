const fs = require('fs');
const bcrypt = require('bcrypt');
var users = {
    "users": [
      {
        "username": "abebe",
        "password": "password"
      },
      {
        "username": "hana",
        "password": "password"
      }
    ]
  };

var medicineInfo = {
  "medicine": [
    {
      "name": "Amoxaicilin",
      "Description": "This is the description of Amoxacilin",
      "stock": "15"
    },
    {
      "name": "Ampiciline",
      "Description": "This is the description of Ampiciline",
      "stock": "25"
    },
    {
      "name": "Aspirin",
      "Description": "This is the description of Aspirin",
      "stock": "47"
    },
    {
      "name": "Busulfanil",
      "Description": "This is the description of Busulfan",
      "stock": "88"
    },
    {
      "name": "Calcium",
      "Description": "This is the description of Calcium",
      "stock": "17"
    },
    {
      "name": "Diclofenac",
      "Description": "This is the description of Diclofenac",
      "stock": "34"
    },
    {
      "name": "Hydrocortizone",
      "Description": "This is the description of Hydrocortizone",
      "stock": "95"
    },
    {
      "name": "ImmuneoGlobulin",
      "Description": "This is the description of ImmuneoGlobulin",
      "stock": "41"
    },
    {
      "name": "Magnisium",
      "Description": "This is the description of Magnisium",
      "stock": "73"
    },
    {
      "name": "Penicilin",
      "Description": "This is the description of Penicilin",
      "stock": "11"
    },
    {
      "name": "Ritonavir",
      "Description": "This is the description of Ritonavir",
      "stock": "9"
    },
    {
      "name": "Warfarin",
      "Description": "This is the description of Warfarin",
      "stock": "42"
    },
    {
      "name": "Zidovudine",
      "Description": "This is the description of Zidovudine",
      "stock": "11"
    }
  ]
};
async function changePassword() {
    for(var i = 0; i < 2; i++){
        const hash = await bcrypt.hash(users.users[i].password, 10)
        users.users[i].password = hash;
    }
    return users;
}

// changePassword().then((hashedUsers) => {
//     fs.writeFile("./database/users.txt", JSON.stringify(hashedUsers), 'utf8', (err)=> {
//         if(err) {
//             return console.log(err)
//         }
//         console.log("File Saved")
//     })
// })

function writeMedicine() {
  fs.writeFile("./database/medcineInfo.txt", JSON.stringify(medicineInfo), "utf8", (err) => {
    if (err) {
      return console.log(err)
    }
    else 
      console.log("File saved");
  })
}

writeMedicine();