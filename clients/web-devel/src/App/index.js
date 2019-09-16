import GameView from '~/views/GameView';
import importedComponent from 'react-imported-component';
import LandingView from '~/views/LandingView';
import Loading from '~/components/Loading';
import React from 'react';
import StreamConnection from './StreamConnection';
import { Switch, BrowserRouter as Router, Route } from 'react-router-dom';

const PerformanceView = importedComponent(
  () => import(/* webpackChunkName:'PerformanceView' */ '../views/PerformanceView'),
  { LoadingComponent: Loading }
);

const NotFound = importedComponent(
  () => import(/* webpackChunkName:'NotFound' */ '../components/NotFound'),
  { LoadingComponent: Loading }
);

const App = () => {
  return (
    <Router>
      <>
        <StreamConnection />
        <Switch>
          <Route exact path="/" component={LandingView} />
          <Route exact path="/performance" component={PerformanceView} />
          <Route path="/games/:gameId" component={GameView} />
          <Route component={NotFound} />
        </Switch>
      </>
    </Router>
  );
};

export default App;
