import React from "react";

import "./errorMessage.css"

export default function ({error}) {
    return error ? <div className="errorMessage">{error}</div> : <></>
}