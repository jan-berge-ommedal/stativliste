import React from "react";
import "./stations.css"

function Station({name, num_bikes_available, num_docks_available}) {
    return (
        <tr>
            <td className="left">{name}</td>
            <td>{num_bikes_available}</td>
            <td>{num_docks_available}</td>
        </tr>
    );
}

export default function ({stations}) {
    if (!stations) {
        return <></>;
    }
    return (
        <table className="stations">
            <tr>
                <th>Stativ</th>
                <th>Ledige sykler</th>
                <th>Ledige låser</th>
            </tr>
            {stations.map(Station)}
        </table>
    )
}