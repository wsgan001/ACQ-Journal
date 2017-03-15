set(gcf,'color',[1, 1, 1]);
set(gcf,'unit','centimeters','position',[10 5 9 7]);

x=[1, 2, 3, 4, 5];
y1 = [6152.98368	6493.073299	6856.191481	7313.104633	7804.371913];
y2 = [285.2561256	833.7990651	1388.694795	2012.267375	2609.694306];
y3 = [21.75589265	28.29044734	45.28982468	51.82908155	68.33300384];

p1= semilogy(x, y1, '-k^');
hold on;
p2 = semilogy(x, y2, '-ko');
hold on;
p3 = semilogy(x, y3, '-kd');

xlabel('the number of keywords in S');
ylabel('time (ms)');

axis([0.5 5.5 0.0 300000]);

set(gca, 'xtick', 1:5, 'XTickLabel', {'1','3','5','7','9'});
leg=legend('basic-g','basic-w','Dec', 2);
set(leg,'edgecolor','white');

set(gca, 'FontSize', 12);
set(get(gca, 'XLabel'), 'FontSize',12);
set(get(gca, 'YLabel'), 'FontSize',12);
set(findall(gcf,'type','line'),'linewidth',1.5);
set(gca, 'LineWidth', 1.5);