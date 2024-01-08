import * as React from 'react';
import Jumbotron from 'react-bootstrap/Jumbotron';
import Form from 'react-bootstrap/Form';
import styles from './index.css';
import writeLoginStart from './writeLoginStart';
import writeLogin from './writeLogin';

const LoginView = (props) => {
  const [mode, setMode] = React.useState('phone');
  const [phoneNumber, setPhoneNumber] = React.useState('');
  const [otp, setOtp] = React.useState('');
  const focusRef = React.useCallback(node => node && node.focus(), []);

  const onChangePhoneNumber = React.useCallback((e) => {
    const phoneNumber = e.target.value;
    const phoneNumberValid = phoneNumber.replace('+1', '');
    setPhoneNumber(phoneNumber);
    if (mode == 'phone' && phoneNumberValid.length >= 10) {
      setMode('otp-issued');
      writeLoginStart({ phoneNumber });
    }
  });

  const onChangeOtp = React.useCallback((e) => {
    const otp = e.target.value;
    setOtp(otp);
    if (mode == 'otp-issued' && otp.length >= 4) {
      writeLogin({
        phoneNumber,
        otp
      }).then(r => props.onLogin(r.data.login));
    }
  });

  const phoneNumberEl = mode == 'phone' ? (
    <Form.Group controlId='phone-number'>
      <Form.Label>Log in using your phone number</Form.Label>
      <Form.Control
        maxLength={10}
        size='lg'
        type='text'
        value={phoneNumber}
        placeholder='Enter phone number'
        onChange={onChangePhoneNumber}
        ref={focusRef}
      />
    </Form.Group>
  ) : null;

  const otpEl = mode == 'otp-issued' ? (
    <Form.Group controlId='otp'>
      <Form.Label>Enter the verification code sent to your phone</Form.Label>
      <Form.Control
        maxLength={4}
        size='lg'
        type='text'
        value={otp}
        placeholder='Enter verification code'
        onChange={onChangeOtp}
        ref={focusRef}
      />
    </Form.Group>
  ) : null;

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
          {phoneNumberEl}
          {otpEl}
        </Form>
      </div>
    </>
  );
}

export default LoginView;
