import {
  entityTableSelector,
  entityDetailsButtonSelector,
  entityDetailsBackButtonSelector,
  entityCreateButtonSelector,
  entityCreateSaveButtonSelector,
  entityCreateCancelButtonSelector,
  entityEditButtonSelector,
  entityDeleteButtonSelector,
  entityConfirmDeleteButtonSelector,
} from '../../support/entity';

describe('Enterprise e2e test', () => {
  const enterprisePageUrl = '/enterprise';
  const enterprisePageUrlPattern = new RegExp('/enterprise(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const enterpriseSample = {
    companyName: 'devant glouglou',
    businessRegisterNumber: 'parce que lors de',
    uniqueIdentificationNumber: 'tomber disputer',
    email: 'Uc7AA@#YK$%Q.0',
    businessPhone: 'pousser',
  };

  let enterprise;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/enterprises+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/enterprises').as('postEntityRequest');
    cy.intercept('DELETE', '/api/enterprises/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (enterprise) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/enterprises/${enterprise.id}`,
      }).then(() => {
        enterprise = undefined;
      });
    }
  });

  it('Enterprises menu should load Enterprises page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('enterprise');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Enterprise').should('exist');
    cy.url().should('match', enterprisePageUrlPattern);
  });

  describe('Enterprise page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(enterprisePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Enterprise page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/enterprise/new$'));
        cy.getEntityCreateUpdateHeading('Enterprise');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', enterprisePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/enterprises',
          body: enterpriseSample,
        }).then(({ body }) => {
          enterprise = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/enterprises+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/enterprises?page=0&size=20>; rel="last",<http://localhost/api/enterprises?page=0&size=20>; rel="first"',
              },
              body: [enterprise],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(enterprisePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Enterprise page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('enterprise');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', enterprisePageUrlPattern);
      });

      it('edit button click should load edit Enterprise page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Enterprise');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', enterprisePageUrlPattern);
      });

      it('edit button click should load edit Enterprise page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Enterprise');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', enterprisePageUrlPattern);
      });

      it('last delete button click should delete instance of Enterprise', () => {
        cy.intercept('GET', '/api/enterprises/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('enterprise').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', enterprisePageUrlPattern);

        enterprise = undefined;
      });
    });
  });

  describe('new Enterprise page', () => {
    beforeEach(() => {
      cy.visit(`${enterprisePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Enterprise');
    });

    it('should create an instance of Enterprise', () => {
      cy.get(`[data-cy="companyName"]`).type("responsable à l'instar de");
      cy.get(`[data-cy="companyName"]`).should('have.value', "responsable à l'instar de");

      cy.get(`[data-cy="businessRegisterNumber"]`).type('contre chef de cuisine');
      cy.get(`[data-cy="businessRegisterNumber"]`).should('have.value', 'contre chef de cuisine');

      cy.get(`[data-cy="uniqueIdentificationNumber"]`).type('bon police alors que');
      cy.get(`[data-cy="uniqueIdentificationNumber"]`).should('have.value', 'bon police alors que');

      cy.get(`[data-cy="businessDomicile"]`).type('triangulaire dessous efficace');
      cy.get(`[data-cy="businessDomicile"]`).should('have.value', 'triangulaire dessous efficace');

      cy.get(`[data-cy="email"]`).type('YBD)X@FZkY.M`m');
      cy.get(`[data-cy="email"]`).should('have.value', 'YBD)X@FZkY.M`m');

      cy.get(`[data-cy="businessPhone"]`).type('glouglou fabriquer');
      cy.get(`[data-cy="businessPhone"]`).should('have.value', 'glouglou fabriquer');

      cy.get(`[data-cy="country"]`).select('SENEGAL');

      cy.get(`[data-cy="city"]`).type('Perpignan');
      cy.get(`[data-cy="city"]`).should('have.value', 'Perpignan');

      cy.setFieldImageAsBytesOfEntity('businessLogo', 'integration-test.png', 'image/png');

      cy.setFieldImageAsBytesOfEntity('mapLocator', 'integration-test.png', 'image/png');

      // since cypress clicks submit too fast before the blob fields are validated
      cy.wait(200); // eslint-disable-line cypress/no-unnecessary-waiting
      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        enterprise = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', enterprisePageUrlPattern);
    });
  });
});
