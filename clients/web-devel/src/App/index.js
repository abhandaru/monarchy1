import importedComponent from 'react-imported-component';
import LandingView from '~/views/LandingView';
import Loading from '~/components/Loading';
import React from 'react';
import StreamConnection from './StreamConnection';
import { Switch, BrowserRouter as Router, Route } from 'react-router-dom';

const AysncPerformanceView = importedComponent(
  () => import(/* webpackChunkName:'PerformanceView' */ '../views/PerformanceView'),
  { LoadingComponent: Loading }
);

const AsyncNotFound = importedComponent(
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
          <Route exact path="/performance" component={AysncPerformanceView} />
          <Route component={AsyncNotFound} />
        </Switch>
      </>
    </Router>
  );
};

export default App;
