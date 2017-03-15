
set(gcf,'color',[1, 1, 1]);
set(gcf,'unit','centimeters','position',[10 5 9 7]);

x=[1, 2, 3, 4, 5];
y1 = [16041.33001	18494.2448	21324.87153	24870.82521	29151.68506];
y2 = [1889.788917	5889.738489	9992.518462	15056.80496	21046.0396];
y3 = [12.20580605	26.68423345	41.50446283	54.49971423	81.23730553];

p1= semilogy(x, y1, '-k^');
hold on;
p2 = semilogy(x, y2, '-ko');
hold on;
p3 = semilogy(x, y3, '-kd');

xlabel('the number of keywords in S');
ylabel('time (ms)');

axis([0.5 5.5 0.0 2000000]);


set(gca, 'xtick', 1:5, 'XTickLabel', {'1','3','5','7','9'});
leg=legend('basic-g','basic-w','Dec', 2);
set(leg,'edgecolor','white');

set(gca, 'FontSize', 12);
set(get(gca, 'XLabel'), 'FontSize',12);
set(get(gca, 'YLabel'), 'FontSize',12);
set(findall(gcf,'type','line'),'linewidth',1.5);
set(gca, 'LineWidth', 1.5);