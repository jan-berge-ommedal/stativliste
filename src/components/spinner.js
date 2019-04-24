import React from "react";

import "./spinner.css";

export default function ({visible}) {
    return visible ? <div className="spinner"/> : <></>
}