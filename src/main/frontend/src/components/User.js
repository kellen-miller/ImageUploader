import React from "react";

function User({userProfileId, username, userProfileImageLink, baseUrl}) {

    return (
        <div>
            {userProfileId &&
            <img
                src={`${baseUrl}/${userProfileId}/image/download`}
                alt=""
            />
            }
            <br/><br/>
            <h1>{username}</h1>
            <p>{userProfileId}</p>
            <p>{userProfileImageLink}</p>
            <br/>
        </div>
    )
}

export default User