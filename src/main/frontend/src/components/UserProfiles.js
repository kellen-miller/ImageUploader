import axios from "axios";
import React, {useCallback, useEffect, useState} from "react";
import {useDropzone} from "react-dropzone";

const UserProfiles = () => {
    const baseUrl = "http://localhost:8080/api/v1/user-profile"

    const [userProfiles, setUserProfiles] = useState([]);

    const fetchUserProfiles = () => {
        axios.get(baseUrl)
            .then(response => {
                setUserProfiles(response.data);
            })
    }

    useEffect(() => {
        fetchUserProfiles();
    }, []);

    function Dropzone({userProfileId}) {
        const onDrop = useCallback(acceptedFiles => {
            const file = acceptedFiles[0]
            console.log(file)
            const formData = new FormData()
            formData.append("file", file)

            axios.post(
                baseUrl + "/" + userProfileId + "/image/upload",
                formData,
                {
                    headers: {
                        "Content-Type": "multipart/form-data"
                    }
                }
            ).then(() => {
                console.log("File uploaded successfully")
            }).catch(err => {
                console.log(err)
            })
        }, [])
        const {getRootProps, getInputProps, isDragActive} = useDropzone({onDrop})

        return (
            <div {...getRootProps()}>
                <input {...getInputProps()} />
                {
                    isDragActive ?
                        <p>Drop the image here ...</p> :
                        <p>Drag 'n' drop profile image, or click to select profile image</p>
                }
            </div>
        )
    }

    return userProfiles.map((userProfile, index) => {
        return (
            <div key={index}>
                {userProfile.userProfileId &&
                <img src={baseUrl + "/" + userProfile.userProfileId + "/image/download"} alt={null}/>
                }
                <br/><br/>
                <h1>{userProfile.username}</h1>
                <p>{userProfile.userProfileId}</p>
                <p>{userProfile.userProfileImageLink}</p>
                <Dropzone {...userProfile} />
                <br/>
            </div>
        )
    })
}

export default UserProfiles;