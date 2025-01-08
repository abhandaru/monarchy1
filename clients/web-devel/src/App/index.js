import AuthConnection from './AuthConnection';
import GameView from '~/views/GameView';
import importedComponent from 'react-imported-component';
import LandingView from '~/views/LandingView';
import Loading from '~/components/Loading';
import React from 'react';
import StreamConnection from './StreamConnection';
import { Switch, BrowserRouter as Router, Route } from 'react-router-dom';

const ProfileView = importedComponent(
  () => import(/* webpackChunkName:'MatchesView' */ '../views/ProfileView'),
  { LoadingComponent: Loading }
);

const MatchesView = importedComponent(
  () => import(/* webpackChunkName:'MatchesView' */ '../views/MatchesView'),
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
      <AuthConnection />
      <StreamConnection />
        <Switch>
          <Route exact path="/" component={LandingView} />
          <Route exact path="/profile" component={ProfileView} />
          <Route exact path="/matches" component={MatchesView} />
          <Route path="/games/:gameId" component={GameView} />
          <Route component={NotFound} />
        </Switch>
      </>
    </Router>
  );
};

export default App;
