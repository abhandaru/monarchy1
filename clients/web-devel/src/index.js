import './index.css';
import App from '~/App';
import React from 'react';
import ReactDOM from 'react-dom/client';
import Reducer from '~/state/reducer';
import thunk from 'redux-thunk';
import { AppContainer } from 'react-hot-loader';
import { applyMiddleware, createStore, compose } from 'redux'
import { Provider } from 'react-redux'

const enhancers = window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__ || compose;
const store = createStore(Reducer, {}, enhancers(applyMiddleware(thunk)));

const root = ReactDOM.createRoot(document.getElementById('root'));
const render = Component => {
  root.render(
    <AppContainer>
      <Provider store={store}>
        <Component />
      </Provider>
    </AppContainer>
  );
};

render(App);
if (module.hot) module.hot.accept('./App', () => render(App));
