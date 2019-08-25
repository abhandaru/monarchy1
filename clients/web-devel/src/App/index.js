import Dashboard from '../Dashboard';
import importedComponent from 'react-imported-component';
import Loading from '../components/Loading';
import React from 'react';
import { Switch, BrowserRouter as Router, Route } from 'react-router-dom';

const AysncMatchmaking = importedComponent(
  () => import(/* webpackChunkName:'Matchmaking' */ '../Matchmaking'),
  { LoadingComponent: Loading }
);

const AsyncNotFound = importedComponent(
  () => import(/* webpackChunkName:'NotFound' */ '../components/NotFound'),
  { LoadingComponent: Loading }
);

const App = () => {
  return (
    <Router>
      <div>
        <Switch>
          <Route exact path="/" component={Dashboard} />
          <Route exact path="/matchmaking" component={AysncMatchmaking} />
          <Route component={AsyncNotFound} />
        </Switch>
      </div>
    </Router>
  );
};

export default App;
