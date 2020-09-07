import axios from "axios";
import React, {useEffect, useState} from "react";
import User from "./User";
import Dropzone from "./Dropzone";

function UserProfiles() {
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
    }, []); //empty array means only run onMount

    return (
        <div>
            {userProfiles
                .sort((a, b) => (a.username > b.username) ? 1 : -1)
                .map((userProfile, index) => {
                    return (
                        <div key={index}>
                            <User {...userProfile} baseUrl={baseUrl}/>
                            <Dropzone {...userProfile} baseUrl={baseUrl}/>
                        </div>
                    )
                })
            }
        </div>
    )
}

export default UserProfiles;