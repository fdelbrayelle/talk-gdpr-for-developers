import React, { useState } from 'react';
import { Button, Input, Radio, Spacer, Text } from '@nextui-org/react';
import { Call, Home, Password, User } from 'react-iconly';

import { useForm } from 'react-hook-form';
import { customer } from '@/about/services/customer';

import './About.scss';

export const About = () => {
  const { register, handleSubmit } = useForm();
  const [error, setError] = useState<boolean>(false);
  const [success, setSuccess] = useState<any>(false);

  const onSubmit = (loginData: any) => {
    console.log(loginData);
    if (
      loginData.lastname &&
      loginData.firstname &&
      loginData.address.number &&
      loginData.address.street &&
      loginData.address.city &&
      loginData.address.zipCode &&
      loginData.address.country &&
      loginData.phoneNumber &&
      loginData.accept
    ) {
      customer({ ...loginData }, setSuccess);
      setError(false);
    } else setError(true);
  };
  return (
    <div className="About">
      <div>
        <form onSubmit={handleSubmit(onSubmit)}>
          <Input
            fullWidth
            clearable
            bordered
            aria-label="modal-login-username"
            data-testid="modal-login-username"
            color="primary"
            size="lg"
            label="Nom"
            placeholder="Votre nom"
            contentLeft={<User />}
            {...register('lastname')}
          />
          {/* dans le ...register t'ajoute le nom de la donnée que tu veux renvoyer au back */}
          <Spacer y={1} />
          <Input
            fullWidth
            clearable
            bordered
            aria-label="modal-login-username"
            data-testid="modal-login-username"
            color="primary"
            size="lg"
            label="Prénom"
            placeholder="Votre prénom"
            contentLeft={<User />}
            {...register('firstname')}
          />
          <Spacer y={1} />
          <Input
            fullWidth
            clearable
            bordered
            aria-label="modal-login-username"
            data-testid="modal-login-username"
            color="primary"
            size="lg"
            label="Numéro de téléphone"
            contentLeft={<Call />}
            placeholder="+33 ..."
            {...register('phoneNumber')}
          />
          <Spacer y={1} />
          <div className="address-block">
            <div className="address-number">
              <Input
                clearable
                bordered
                aria-label="modal-login-username"
                data-testid="modal-login-username"
                color="primary"
                size="lg"
                label="Numéro de rue"
                placeholder="Votre numéro de rue"
                contentLeft={<Home />}
                {...register('address.number')}
              />
            </div>
            <div id="address-street" className="address-street">
              <Input
                fullWidth
                clearable
                bordered
                aria-label="modal-login-username"
                data-testid="modal-login-username"
                color="primary"
                size="lg"
                label="Rue"
                placeholder="Votre rue"
                contentLeft={<Home />}
                {...register('address.street')}
              />
            </div>
            <Input
              className="address-city"
              clearable
              bordered
              aria-label="modal-login-username"
              data-testid="modal-login-username"
              color="primary"
              size="lg"
              label="Ville"
              placeholder="Votre ville"
              contentLeft={<Home />}
              {...register('address.city')}
            />
            <Input
              className="address-zip-code"
              clearable
              bordered
              aria-label="modal-login-username"
              data-testid="modal-login-username"
              color="primary"
              size="lg"
              label="Code postal"
              placeholder="Votre code postal"
              contentLeft={<Home />}
              {...register('address.zipCode')}
            />
            <Input
              className="address-country"
              clearable
              bordered
              aria-label="modal-login-username"
              data-testid="modal-login-username"
              color="primary"
              size="lg"
              label="Pays"
              placeholder="Votre pays"
              contentLeft={<Home />}
              {...register('address.country')}
            />
          </div>
          <Spacer y={1} />
          <Radio.Group size="sm" value="homme" row>
            <label>Sexe :</label>
            <Radio size="sm" value="homme">
              Homme
            </Radio>
            <Radio size="sm" value="femme">
              Femme
            </Radio>
            <Radio size="sm" value="autre">
              Autre
            </Radio>
          </Radio.Group>
          <Spacer y={1} />
          <input type="checkbox" {...register('accept')} />
          <label>J'accepte les conditions</label>
          <Spacer y={1} />
          <Button data-testid="submit-button" className="submit-button" type="submit" shadow auto>
            Enregistrer mes infos
          </Button>
          <Spacer y={1} />
          {error && (
            <Text data-testid="error-message" size={13} color="error">
              Veuillez remplir les champs ci dessus
            </Text>
          )}
        </form>
        {success === true && <p>Merci d'avoir renseigné vos informations</p>}
      </div>
    </div>
  );
};
