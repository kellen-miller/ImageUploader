import axios from "axios";
import React, {useEffect} from "react";

const UserProfiles = () => {
    const fetchUserProfiles = () => {
        axios.get("http://localhost:8080/api/v1/user-profile")
            .then(response => {
                console.log(response);
            })
    }

    useEffect(() => {
        fetchUserProfiles();
    }, []);

    return <h1>Hello</h1>
}

export default UserProfiles;