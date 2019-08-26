import React from 'react';
import Jumbotron from 'react-bootstrap/Jumbotron';
import Form from 'react-bootstrap/Form';
import styles from './index.css';

const LoginView = (props) => {
  return (
    <>
      <Jumbotron>
        <h1>Welcome to Monarchy!</h1>
        <p>
          This a low-fidelity debugging client to exercise the API.
        </p>
      </Jumbotron>
      <div className={styles.login}>
        <Form>
          <Form.Group controlId="phone-number">
            <Form.Label>Log in using your phone number</Form.Label>
            <Form.Control size="lg" type="text" placeholder="Enter phone number" />
          </Form.Group>
        </Form>
      </div>
    </>
  );
}

export default LoginView;
