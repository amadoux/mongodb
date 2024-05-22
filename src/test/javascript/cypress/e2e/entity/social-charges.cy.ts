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

describe('SocialCharges e2e test', () => {
  const socialChargesPageUrl = '/social-charges';
  const socialChargesPageUrlPattern = new RegExp('/social-charges(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const socialChargesSample = { amount: 5938, commentText: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=' };

  let socialCharges;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/social-charges+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/social-charges').as('postEntityRequest');
    cy.intercept('DELETE', '/api/social-charges/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (socialCharges) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/social-charges/${socialCharges.id}`,
      }).then(() => {
        socialCharges = undefined;
      });
    }
  });

  it('SocialCharges menu should load SocialCharges page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('social-charges');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('SocialCharges').should('exist');
    cy.url().should('match', socialChargesPageUrlPattern);
  });

  describe('SocialCharges page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(socialChargesPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create SocialCharges page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/social-charges/new$'));
        cy.getEntityCreateUpdateHeading('SocialCharges');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', socialChargesPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/social-charges',
          body: socialChargesSample,
        }).then(({ body }) => {
          socialCharges = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/social-charges+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/social-charges?page=0&size=20>; rel="last",<http://localhost/api/social-charges?page=0&size=20>; rel="first"',
              },
              body: [socialCharges],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(socialChargesPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details SocialCharges page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('socialCharges');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', socialChargesPageUrlPattern);
      });

      it('edit button click should load edit SocialCharges page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('SocialCharges');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', socialChargesPageUrlPattern);
      });

      it('edit button click should load edit SocialCharges page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('SocialCharges');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', socialChargesPageUrlPattern);
      });

      it('last delete button click should delete instance of SocialCharges', () => {
        cy.intercept('GET', '/api/social-charges/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('socialCharges').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', socialChargesPageUrlPattern);

        socialCharges = undefined;
      });
    });
  });

  describe('new SocialCharges page', () => {
    beforeEach(() => {
      cy.visit(`${socialChargesPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('SocialCharges');
    });

    it('should create an instance of SocialCharges', () => {
      cy.get(`[data-cy="spentDate"]`).type('2024-05-22T02:19');
      cy.get(`[data-cy="spentDate"]`).blur();
      cy.get(`[data-cy="spentDate"]`).should('have.value', '2024-05-22T02:19');

      cy.get(`[data-cy="spentType"]`).select('SHIFT');

      cy.get(`[data-cy="statusCharges"]`).select('IN_PROGRESS');

      cy.get(`[data-cy="amount"]`).type('26389');
      cy.get(`[data-cy="amount"]`).should('have.value', '26389');

      cy.get(`[data-cy="commentText"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="commentText"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        socialCharges = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', socialChargesPageUrlPattern);
    });
  });
});
