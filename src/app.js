import React, {useState} from 'react';
import logo from './logo.svg';
import './app.css';
import {fetchData} from "./api";
import ErrorMessage from './components/errorMessage'
import Stations from './components/stations'
import Spinner from './components/spinner'

function App() {
    const [data, setData] = useState({loading: true});
    const [hasFetched, setHasFetched] = useState(false);

    if (!hasFetched) {
        setHasFetched(true);
        fetchData(setData);
    }

    const {error, stations, loading} = data;
    return (
        <div className="app">
            <img src={logo} className="logo" alt="logo"/>
            <ErrorMessage error={error}/>
            <Spinner visible={loading}/>
            <Stations stations={stations}/>
        </div>
    );
}

export default App;
